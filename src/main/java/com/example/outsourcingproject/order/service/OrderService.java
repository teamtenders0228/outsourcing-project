package com.example.outsourcingproject.order.service;

import com.example.outsourcingproject.menu.dto.response.MenuResponseDto;
import com.example.outsourcingproject.menu.entity.Menu;
import com.example.outsourcingproject.menu.repository.MenuRepository;
import com.example.outsourcingproject.order.dto.request.OrderSaveRequestDto;
import com.example.outsourcingproject.order.dto.response.*;
import com.example.outsourcingproject.order.entity.Order;
import com.example.outsourcingproject.order.enums.Status;
import com.example.outsourcingproject.order.repository.OrderRepository;
import com.example.outsourcingproject.orderItem.entity.OrderItem;
import com.example.outsourcingproject.orderItem.repository.OrderItemRepository;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.store.repository.StoreRepository;
import com.example.outsourcingproject.user.entity.User;
import com.example.outsourcingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
    public OrderSaveResponseDto orderSave(Long storeId, List<OrderSaveRequestDto> menus) {
        Long userId = 1L;

        User findUser = userRepository.findByIdOrElseThrow(userId);
        Store findStore = storeRepository.findByIdOrElseThrow(storeId);

        // totalPrice 계산
        Integer totalPrice = 0;

        for(OrderSaveRequestDto menu : menus){
            Menu findMenu = menuRepository.findByIdOrElseThrow(menu.getMenuId());

            totalPrice += findMenu.getPrice() * menu.getCount();
        }

        // newOrder 저장
        Order newOrder = new Order(findUser, findStore, false, Status.PENDING, totalPrice);

        Order savedOrder = orderRepository.save(newOrder);

        // newOrderItem 저장
        for(OrderSaveRequestDto menu : menus){
            Menu findMenu = menuRepository.findByIdOrElseThrow(menu.getMenuId());

            OrderItem newOrderItem = new OrderItem(savedOrder, findMenu, menu.getCount());

            orderItemRepository.save(newOrderItem);
        }

        return new OrderSaveResponseDto("주문이 완료되었습니다.");
    }

    // 수락 - (가게사장님 만 가능)
    @Transactional
    public OrderAcceptResponseDto orderAccept(Long orderId) {
        Order findOrder = orderRepository.findByIdOrElseThrow(orderId);

        findOrder.setStatus(Status.ACCEPT);
        findOrder.setAccepted(true);

        return new OrderAcceptResponseDto("주문이 수락되었습니다.");
    }

    // 취소(거절) - (가게사장님 만 가능)
    @Transactional
    public OrderRejectResponseDto orderReject(Long orderId) {
        Order findOrder = orderRepository.findByIdOrElseThrow(orderId);

        findOrder.setStatus(Status.REJECT);
        findOrder.setAccepted(false);

        return new OrderRejectResponseDto("주문이 거절되었습니다. 불편을 드려 죄송합니다.");
    }

    // 주문 상태 변경 - (가게사장님 만 가능)
    @Transactional
    public OrderStatusResponseDto statusChange(Long orderId, Status status) {
        Order findOrder = orderRepository.findByIdOrElseThrow(orderId);

        Boolean accepted = true;
        String message = "";

        if(status.equals(status.PENDING)){
            accepted = false;
            message = "주문이 접수되었습니다.";
        }
        if(status.equals(status.ACCEPT)){
            message = "주문이 수락되었습니다.";
        }
        if(status.equals(status.COOKING)){
            message = "음식이 조리 중입니다. ";
        }
        if(status.equals(status.DELIVERY)){
            message = "배달이 시작되었습니다.";
        }
        if(status.equals(status.COMPLETE)){
            message = "배달이 완료되었습니다.";
        }
        if(status.equals(status.REJECT)){
            accepted = false;
            message = "주문이 거절되었습니다. 불편을 드려 죄송합니다.";
        }

        findOrder.setStatus(status);
        findOrder.setAccepted(accepted);

        return new OrderStatusResponseDto(message);
    }

    // 사용자별 주문 내역 조회
    public List<UserOrdersResponseDto> findOrdersByUser(Long userId) {
        DecimalFormat formatter = new DecimalFormat("###,###");

        List<Order> findOrders = orderRepository.findAllByUserId(userId);

        List<UserOrdersResponseDto> dtoList = new ArrayList<>();

        for(Order findOrder : findOrders){

            // menuResponseDtoList
            List<MenuResponseDto> menuResponseDtoList = new ArrayList<>();

            List<OrderItem> findOrderItems = orderItemRepository.findAllByOrderId(findOrder.getId());

            Integer totalPrice = 0;

            for(OrderItem findOrderItem : findOrderItems){
                Menu findMenu = menuRepository.findByIdOrElseThrow(findOrderItem.getMenu().getId());

                MenuResponseDto menuResponseDto = new MenuResponseDto(
                        findMenu.getName(),
                        formatter.format(findMenu.getPrice()),
                        findOrderItem.getCount()
                );

                totalPrice += findMenu.getPrice() * findOrderItem.getCount();

                menuResponseDtoList.add(menuResponseDto);
            }

            // dtoList
            Store findStore = storeRepository.findByIdOrElseThrow(findOrder.getStore().getId());

            UserOrdersResponseDto dto = new UserOrdersResponseDto(
                    findStore.getName(),
                    menuResponseDtoList,
                    findStore.getCategory(),
                    findOrder.getStatus(),
                    formatter.format(totalPrice)
            );

            dtoList.add(dto);
        }
        return dtoList;
    }
}
