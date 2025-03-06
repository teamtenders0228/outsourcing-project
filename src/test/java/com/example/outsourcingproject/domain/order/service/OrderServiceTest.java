package com.example.outsourcingproject.domain.order.service;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.menu.dto.response.MenuOrderResponseDto;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.menu.service.MenuService;
import com.example.outsourcingproject.domain.order.dto.request.OrderSaveRequestDto;
import com.example.outsourcingproject.domain.order.dto.response.OrderStatusResponseDto;
import com.example.outsourcingproject.domain.order.dto.response.UserOrdersResponseDto;
import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.order.enums.Status;
import com.example.outsourcingproject.domain.order.repository.OrderRepository;
import com.example.outsourcingproject.domain.orderItem.entity.OrderItem;
import com.example.outsourcingproject.domain.orderItem.repository.OrderItemRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRole;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.outsourcingproject.common.exception.ErrorCode.MENU_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    MenuRepository menuRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    StoreRepository storeRepository;

    @Mock
    OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void order_일반_유저_롤이_아니어서_에러가_난다(){
        // given

        // menus 생성
        OrderSaveRequestDto orderSaveRequestDto = new OrderSaveRequestDto();

        ReflectionTestUtils.setField(orderSaveRequestDto, "menuId", 1L);
        ReflectionTestUtils.setField(orderSaveRequestDto, "count", 1);

        List<OrderSaveRequestDto> menus = new ArrayList<>();
        menus.add(orderSaveRequestDto);

        // when & then
        assertThrows(BaseException.class, () -> orderService.saveOrder(1L, UserRole.OWNER, 1L, menus));
    }

    @ParameterizedTest
    public void 가게의_메뉴가_아니라서_에러가_난다(){
        // given
        Long userId = 1L;
        UserRole userRole = UserRole.USER;
        Long storeId = 1L;

        // (주문) menus 생성
        OrderSaveRequestDto orderSaveRequestDto = new OrderSaveRequestDto();

        ReflectionTestUtils.setField(orderSaveRequestDto, "menuId", 1L);
        ReflectionTestUtils.setField(orderSaveRequestDto, "count", 1);

        List<OrderSaveRequestDto> menus = new ArrayList<>();
        menus.add(orderSaveRequestDto);

        // (가게) menuList 생성
        List<Menu> menuList = new ArrayList<>();

        User newOwner = new User(2L, "홍길동", "ijieun@gmail.com", "Password123@", "010-1234-1234", "서울특별시 마장동 388-12 204호", UserRole.OWNER, false);
        Store newStore = new Store(storeId, "동해반점", "서울특별시 마장동 388-12", "010-0000-0000", StoreCategory.CHINESE, 5000, LocalTime.of(9, 0, 0), LocalTime.of(22, 0, 0), 5.0, false, null, newOwner);
        Menu newMenu = new Menu(2L, "짬뽕", 6000, false, newStore);

        menuList.add(newMenu);

        given(menuRepository.findAllByStoreId(any())).willReturn(menuList);

        // when
        BaseException exception = assertThrows(BaseException.class, () -> {
            orderService.saveOrder(userId, userRole, storeId, menus);
        });

        // then
        assertEquals(MENU_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    public void 롤이_일반_유저이고_메뉴가_있으면_주문_요청이_가능하다(){
        // given
        Long userId = 1L;
        UserRole userRole = UserRole.USER;
        Long storeId = 1L;

        // (주문) menus 생성
        OrderSaveRequestDto orderSaveRequestDto = new OrderSaveRequestDto();

        ReflectionTestUtils.setField(orderSaveRequestDto, "menuId", 1L);
        ReflectionTestUtils.setField(orderSaveRequestDto, "count", 1);

        List<OrderSaveRequestDto> menus = new ArrayList<>();
        menus.add(orderSaveRequestDto);

        // (가게) menuList 생성
        List<Menu> menuList = new ArrayList<>();

        User newOwner = new User(2L, "홍길동", "ijieun@gmail.com", "Password123@", "010-1234-1234", "서울특별시 마장동 388-12 204호", UserRole.OWNER, false);
        Store newStore = new Store(storeId, "동해반점", "서울특별시 마장동 388-12", "010-0000-0000", StoreCategory.CHINESE, 5000, LocalTime.of(9, 0, 0), LocalTime.of(22, 0, 0), 5.0, false, null, newOwner);
        Menu newMenu = new Menu(1L, "짜장면", 5000, false, newStore);

        menuList.add(newMenu);

        given(menuRepository.findAllByStoreId(storeId)).willReturn(menuList);

        // totalPrice 계산
        Integer totalPrice = 0;

        for(OrderSaveRequestDto menu : menus){
            given(menuRepository.findByIdOrElseThrow(menu.getMenuId())).willReturn(newMenu);

            totalPrice += newMenu.getPrice() * menu.getCount();
        }

        // order 생성
        User newUser = new User(userId, "홍길동", "ijieun@gmail.com", "Password123@", "010-1234-1234", "서울특별시 마장동 388-12 204호", userRole, false);

        given(userRepository.findByIdOrElseThrow(userId)).willReturn(newUser);
        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(newStore);

        Order newOrder = new Order(1L, newUser, newStore, false, Status.PENDING, totalPrice);

        given(orderRepository.save(any())).willReturn(newOrder);

        // newOrderItem 생성
        for(OrderSaveRequestDto menu : menus){
            given(menuRepository.findByIdOrElseThrow(menu.getMenuId())).willReturn(newMenu);

            OrderItem newOrderItem = new OrderItem(newOrder, newMenu, menu.getCount());

            given(orderItemRepository.save(any())).willReturn(newOrderItem);
        }

        // when
        OrderStatusResponseDto responseDto = orderService.saveOrder(userId, userRole, storeId, menus);

        // then
        assertThat(responseDto.getOrderId()).isEqualTo(newOrder.getId());
    }

    @Test
    public void 가게_사장님이_아니면_에러가_난다() {
        // given
        Long ownerId = 1L;
        Long orderId = 1L;

        // findOrder
        User newOwner = new User(2L, "홍길동", "ijieun@gmail.com", "Password123@", "010-1234-1234", "서울특별시 마장동 388-12 204호", UserRole.OWNER, false);
        User newUser = new User(1L, "홍길동", "ijieun@gmail.com", "Password123@", "010-1234-1234", "서울특별시 마장동 388-12 204호", UserRole.USER, false);
        Store newStore = new Store(1L, "동해반점", "서울특별시 마장동 388-12", "010-0000-0000", StoreCategory.CHINESE, 5000, LocalTime.of(9, 0, 0), LocalTime.of(22, 0, 0), 5.0, false, null, newOwner);
        Order newOrder = new Order(orderId, newUser, newStore, false, Status.PENDING, 5000);

        given(orderRepository.findByIdOrElseThrow(any())).willReturn(newOrder);

        // when
        BaseException exception = assertThrows(BaseException.class, () -> {
            orderService.isStoreOwner(ownerId, orderId);
        });

        // then
        assertEquals(ErrorCode.ORDER_STATUS_ONLY_FOR_OWNER.getMessage(), exception.getMessage());
    }

    @Test
    public void 가게_사장이면_주문_수락을_할_수_있다(){
        // given
        Long ownerId = 2L;
        Long orderId = 1L;

        findOrderAndStoreList(ownerId, orderId, Status.PENDING);

        // when
        OrderStatusResponseDto responseDto = orderService.acceptOrder(ownerId, orderId);

        // then
        assertEquals(responseDto.getMessage(), "주문이 수락되었습니다.");
    }

    @Test
    public void PENDING_은_ACCEPT_로_상태가_변경된다(){
        // given
        Long ownerId = 2L;
        Long orderId = 1L;

        findOrderAndStoreList(ownerId, orderId, Status.PENDING);

        // when
        OrderStatusResponseDto responseDto = orderService.updateProgressStatus(ownerId, orderId);

        // then
        assertEquals(responseDto.getMessage(), "주문이 수락되었습니다.");
    }

    @Test
    public void ACCEPT_는_COOKING_으로_상태가_변경된다(){
        // given
        Long ownerId = 2L;
        Long orderId = 1L;

        findOrderAndStoreList(ownerId, orderId, Status.ACCEPT);

        // when
        OrderStatusResponseDto responseDto = orderService.updateProgressStatus(ownerId, orderId);

        // then
        assertEquals(responseDto.getMessage(), "음식이 조리 중입니다.");
    }

    @Test
    public void COOKING_은_DELIVERY_로_상태가_변경된다(){
        // given
        Long ownerId = 2L;
        Long orderId = 1L;

        findOrderAndStoreList(ownerId, orderId, Status.COOKING);

        // when
        OrderStatusResponseDto responseDto = orderService.updateProgressStatus(ownerId, orderId);

        // then
        assertEquals(responseDto.getMessage(), "배달이 시작되었습니다.");
    }

    @Test
    public void DELIVERY_는_COMPLETE_로_상태가_변경된다(){
        // given
        Long ownerId = 2L;
        Long orderId = 1L;

        findOrderAndStoreList(ownerId, orderId, Status.DELIVERY);

        // when
        OrderStatusResponseDto responseDto = orderService.updateProgressStatus(ownerId, orderId);

        // then
        assertEquals(responseDto.getMessage(), "배달이 완료되었습니다.");
    }

    @Test
    public void COMPLETE_는_상태를_변경할_수_없다(){
        // given
        Long ownerId = 2L;
        Long orderId = 1L;

        findOrderAndStoreList(ownerId, orderId, Status.COMPLETE);

        // when
        OrderStatusResponseDto responseDto = orderService.updateProgressStatus(ownerId, orderId);

        // then
        assertEquals(responseDto.getMessage(), "상태를 변경할 수 없습니다.");
    }

    @Test
    public void REJECT_는_상태를_변경할_수_없다(){
        // given
        Long ownerId = 2L;
        Long orderId = 1L;

        findOrderAndStoreList(ownerId, orderId, Status.REJECT);

        // when
        OrderStatusResponseDto responseDto = orderService.updateProgressStatus(ownerId, orderId);

        // then
        assertEquals(responseDto.getMessage(), "상태를 변경할 수 없습니다.");
    }

    @Test
    public void 가게_사장이면_주문_거절을_할_수_있다(){
        // given
        Long ownerId = 2L;
        Long orderId = 1L;

        findOrderAndStoreList(ownerId, orderId, Status.PENDING);

        // when
        OrderStatusResponseDto responseDto = orderService.changeStatusToReject(ownerId, orderId);

        // then
        assertEquals(responseDto.getMessage(), "주문이 거절되었습니다.");
    }

    public void findOrderAndStoreList(Long ownerId, Long orderId, Status status){
        // findOrder
        User newOwner = new User(2L, "홍길동", "ijieun@gmail.com", "Password123@", "010-1234-1234", "서울특별시 마장동 388-12 204호", UserRole.OWNER, false);
        User newUser = new User(1L, "홍길동", "ijieun@gmail.com", "Password123@", "010-1234-1234", "서울특별시 마장동 388-12 204호", UserRole.USER, false);
        Store newStore = new Store(1L, "동해반점", "서울특별시 마장동 388-12", "010-0000-0000", StoreCategory.CHINESE, 5000, LocalTime.of(9, 0, 0), LocalTime.of(22, 0, 0), 5.0, false, null, newOwner);
        Order newOrder = new Order(orderId, newUser, newStore, false, status, 5000);

        given(orderRepository.findByIdOrElseThrow(any())).willReturn(newOrder);

        // 가게 사장의 storeList
        List<Store> storeList = new ArrayList<>();
        storeList.add(newStore);

        given(storeRepository.findByUser_Id(ownerId)).willReturn(storeList);
    }

    @Test
    public void 누구나_사용자별_주문_내역을_조회할_수_있다(){
        // given
        Long userId = 1L;

        // findUser
        User newUser = new User(1L, "홍길동", "ijieun@gmail.com", "Password123@", "010-1234-1234", "서울특별시 마장동 388-12 204호", UserRole.USER, false);

        given(userRepository.findByIdOrElseThrow(userId)).willReturn(newUser);

        // findOrders
        List<Order> findOrders = new ArrayList<>();

        User newOwner = new User(2L, "홍길동", "ijieun@gmail.com", "Password123@", "010-1234-1234", "서울특별시 마장동 388-12 204호", UserRole.OWNER, false);
        Store newStore = new Store(1L, "동해반점", "서울특별시 마장동 388-12", "010-0000-0000", StoreCategory.CHINESE, 5000, LocalTime.of(9, 0, 0), LocalTime.of(22, 0, 0), 5.0, false, null, newOwner);
        Order newOrder = new Order(1L, newUser, newStore, false, Status.PENDING, 5000);

        findOrders.add(newOrder);

        given(orderRepository.findAllByUserId(userId)).willReturn(findOrders);

        // findOrderItems
        List<OrderItem> orderItemList = new ArrayList<>();

        Menu newMenu = new Menu(1L, "짜장면", 5000, false, newStore);
        OrderItem newOrderItem = new OrderItem(1L, newOrder, newMenu, 1);

        orderItemList.add(newOrderItem);

        given(orderItemRepository.findAllByOrderId(newOrder.getId())).willReturn(orderItemList);

        // menuResponseDtoList
        List<MenuOrderResponseDto> menuOrderResponseDtoList = new ArrayList<>();

        // totalPrice & menuResponseDto
        DecimalFormat formatter = new DecimalFormat("###,###");

        Integer totalPrice = 0;

        for(OrderItem orderItem : orderItemList){
            given(menuRepository.findByIdOrElseThrow(orderItem.getMenu().getId())).willReturn(newMenu);

            MenuOrderResponseDto menuResponseDto = new MenuOrderResponseDto(
                    newMenu.getMenuName(),
                    formatter.format(newMenu.getPrice()),
                    newOrderItem.getCount()
            );

            totalPrice = newMenu.getPrice() * orderItem.getCount();

            menuOrderResponseDtoList.add(menuResponseDto);
        }

        // findStore
        given(storeRepository.findByIdOrElseThrow(newOrder.getStore().getId())).willReturn(newStore);

        // when
        List<UserOrdersResponseDto> userOrdersResponseDtoList = orderService.findOrdersByUser(userId);

        // then
        assertEquals(userOrdersResponseDtoList.get(0).getCategory(), StoreCategory.CHINESE.toString());
        assertEquals(userOrdersResponseDtoList.get(0).getStoreName(), newStore.getStoreName());
        assertEquals(userOrdersResponseDtoList.get(0).getStatus(),  newOrder.getStatus());
        assertEquals(userOrdersResponseDtoList.get(0).getMenu().get(0).getName(), menuOrderResponseDtoList.get(0).getName());
        assertEquals(userOrdersResponseDtoList.get(0).getMenu().get(0).getPrice(), menuOrderResponseDtoList.get(0).getPrice());
        assertEquals(userOrdersResponseDtoList.get(0).getMenu().get(0).getCount(), menuOrderResponseDtoList.get(0).getCount());
        assertEquals(userOrdersResponseDtoList.get(0).getTotalPrice(), formatter.format(totalPrice));
    }
}






















