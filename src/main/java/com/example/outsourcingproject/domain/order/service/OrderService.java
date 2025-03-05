package com.example.outsourcingproject.domain.order.service;

import com.example.outsourcingproject.common.annotation.Trace;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.order.dto.response.*;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.entity.UserRole;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import com.example.outsourcingproject.domain.menu.dto.response.MenuOrderResponseDto;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.order.dto.request.OrderSaveRequestDto;
import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.order.enums.Status;
import com.example.outsourcingproject.domain.order.repository.OrderRepository;
import com.example.outsourcingproject.domain.orderItem.entity.OrderItem;
import com.example.outsourcingproject.domain.orderItem.repository.OrderItemRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.outsourcingproject.common.exception.ErrorCode.MENU_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final OrderItemRepository orderItemRepository;

    // 주문 요청 - (일반 유저만 가능)
    @Trace
    public OrderStatusResponseDto orderSave(Long userId, UserRole userRole, Long storeId, List<OrderSaveRequestDto> menus) {
        // 일반 유저인지 판별
        if(userRole != UserRole.USER) throw new BaseException(ErrorCode.ORDER_ONLY_FOR_REGULAR_USER, null);

        // store 의 menu 가 아닌 경우 예외 처리
        List<Menu> menuList = menuRepository.findAllByStoreId(storeId);
        List<Long> menuIdList = new ArrayList<>();

        for(Menu menu : menuList){
            menuIdList.add(menu.getId());
        }

        for(OrderSaveRequestDto menu : menus){
            if(!menuIdList.contains(menu.getMenuId())) throw new BaseException(MENU_NOT_FOUND, null);
        }

        // totalPrice 계산
        Integer totalPrice = 0;

        for(OrderSaveRequestDto menu : menus){
            Menu findMenu = menuRepository.findByIdOrElseThrow(menu.getMenuId());

            totalPrice += findMenu.getPrice() * menu.getCount();
        }

        // newOrder 저장
        User findUser = userRepository.findByIdOrElseThrow(userId);
        Store findStore = storeRepository.findByIdOrElseThrow(storeId);

        Order newOrder = new Order(findUser, findStore, false, Status.PENDING, totalPrice);

        Order savedOrder = orderRepository.save(newOrder);

        // newOrderItem 저장
        for(OrderSaveRequestDto menu : menus){
            Menu findMenu = menuRepository.findByIdOrElseThrow(menu.getMenuId());

            OrderItem newOrderItem = new OrderItem(savedOrder, findMenu, menu.getCount());

            orderItemRepository.save(newOrderItem);
        }

        return new OrderStatusResponseDto("주문이 완료되었습니다.", savedOrder.getId(), storeId);
    }

    // 수락 - (가게사장님 만 가능)
    @Trace
    @Transactional
    public OrderStatusResponseDto orderAccept(Long userId, Long orderId) {
        // 가게 사장인지 판별
        Order findOrder = orderRepository.findByIdOrElseThrow(orderId);
        List<Store> storeList = storeRepository.findByUser_Id(userId);

        if(!storeList.contains(findOrder.getStore())) throw new BaseException(ErrorCode.ORDER_ACCEPT_ONLY_FOR_OWNER, null);

        // 주문 수락
        findOrder.setStatus(Status.ACCEPT);
        findOrder.setAccepted(true);

        return new OrderStatusResponseDto("주문이 수락되었습니다.", orderId, findOrder.getStore().getId());
    }

    // 주문 상태 변경 - (가게사장님 만 가능)(거절 외 순차적으로 변경)
    @Trace
    @Transactional
    public OrderStatusResponseDto updateProgressStatus(Long userId, Long orderId) {
        // 가게 사장인지 판별
        Order findOrder = orderRepository.findByIdOrElseThrow(orderId);
        List<Store> storeList = storeRepository.findByUser_Id(userId);

        if(!storeList.contains(findOrder.getStore())) throw new BaseException(ErrorCode.ORDER_STATUS_ONLY_FOR_OWNER, null);

        // 메시지 작성
        String message = "";
        Boolean accepted = true;

        if(findOrder.getStatus().equals(Status.PENDING)){
            findOrder.setStatus(Status.ACCEPT);
            message = "주문이 수락되었습니다.";
        }
        else if(findOrder.getStatus().equals(Status.ACCEPT)){
            findOrder.setStatus(Status.COOKING);
            message = "음식이 조리 중입니다.";
        }
        else if(findOrder.getStatus().equals(Status.COOKING)){
            findOrder.setStatus(Status.DELIVERY);
            message = "배달이 시작되었습니다.";
        }
        else if(findOrder.getStatus().equals(Status.DELIVERY)){
            findOrder.setStatus(Status.COMPLETE);
            message = "배달이 완료되었습니다.";
        }
        else if(findOrder.getStatus().equals(Status.COMPLETE)){
            message = "상태를 변경할 수 없습니다.";
        }
        else if(findOrder.getStatus().equals(Status.REJECT)){
            message = "상태를 변경할 수 없습니다.";
            accepted = false;
        }

        findOrder.setAccepted(accepted);

        return new OrderStatusResponseDto(message, orderId, findOrder.getStore().getId());
    }

    // 주문 상태 변경 - (가게사장님 만 가능)(거절)
    @Trace
    @Transactional
    public OrderStatusResponseDto statusChangeReject(Long userId, Long orderId) {
        // 가게 사장인지 판별
        Order findOrder = orderRepository.findByIdOrElseThrow(orderId);
        List<Store> storeList = storeRepository.findByUser_Id(userId);

        if(!storeList.contains(findOrder.getStore())) throw new BaseException(ErrorCode.ORDER_REJECT_ONLY_FOR_OWNER, null);

        // status 변경
        findOrder.setAccepted(false);
        findOrder.setStatus(Status.REJECT);

        return new OrderStatusResponseDto("주문이 거절되었습니다.", orderId, findOrder.getStore().getId());
    }

    // 사용자별 주문 내역 조회
    public List<UserOrdersResponseDto> findOrdersByUser(Long userId) {
        // user 가 없는 회원일 경우
        userRepository.findByIdOrElseThrow(userId);

        // 가격 포맷
        DecimalFormat formatter = new DecimalFormat("###,###");

        List<Order> findOrders = orderRepository.findAllByUserId(userId);

        List<UserOrdersResponseDto> dtoList = new ArrayList<>();

        for(Order findOrder : findOrders){

            // menuResponseDtoList
            List<MenuOrderResponseDto> menuResponseDtoList = new ArrayList<>();

            List<OrderItem> findOrderItems = orderItemRepository.findAllByOrderId(findOrder.getId());

            Integer totalPrice = 0;

            for(OrderItem findOrderItem : findOrderItems){
                Menu findMenu = menuRepository.findByIdOrElseThrow(findOrderItem.getMenu().getId());

                MenuOrderResponseDto menuResponseDto = new MenuOrderResponseDto(
                        findMenu.getMenuName(),
                        formatter.format(findMenu.getPrice()),
                        findOrderItem.getCount()
                );

                totalPrice += findMenu.getPrice() * findOrderItem.getCount();

                menuResponseDtoList.add(menuResponseDto);
            }

            // dtoList
            Store findStore = storeRepository.findByIdOrElseThrow(findOrder.getStore().getId());

            UserOrdersResponseDto dto = new UserOrdersResponseDto(
                    findStore.getStoreName(),
                    menuResponseDtoList,
                    findStore.getCategory().toString(),
                    findOrder.getStatus(),
                    formatter.format(totalPrice)
            );

            dtoList.add(dto);
        }
        return dtoList;
    }
}
