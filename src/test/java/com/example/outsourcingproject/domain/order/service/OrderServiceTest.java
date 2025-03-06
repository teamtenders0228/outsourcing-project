package com.example.outsourcingproject.domain.order.service;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.menu.service.MenuService;
import com.example.outsourcingproject.domain.order.dto.request.OrderSaveRequestDto;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

//    @Test
//    public void 가게의_메뉴가_아닌_경우(){
//        // given
//        Long userId = 1L;
//        Long ownerId = 2L;
//        UserRole userRole = UserRole.USER;
//        Long storeId = 1L;
//
//        // menus 생성
//        OrderSaveRequestDto orderSaveRequestDto = new OrderSaveRequestDto();
//
//        ReflectionTestUtils.setField(orderSaveRequestDto, "menuId", 1L);
//        ReflectionTestUtils.setField(orderSaveRequestDto, "count", 1);
//
//        List<OrderSaveRequestDto> menus = new ArrayList<>();
//        menus.add(orderSaveRequestDto);
//
//        // menuList 생성
//        User newOwner = new User(ownerId, "홍길동", "ijieun@gmail.com", "Password123@", "010-1234-1234", "서울특별시 마장동 388-12 204호", UserRole.OWNER, false);
//        Store newStore = new Store(storeId, "동해반점", "서울특별시 마장동 388-12", "010-0000-0000", StoreCategory.CHINESE, 5000, LocalTime.of(9, 0, 0), LocalTime.of(22, 0, 0), 5.0, false, null, newOwner);
//        Menu newMenu1 = new Menu(1L, "짜장면", 5000, false, newStore);
//        Menu newMenu2 = new Menu(2L, "짬뽕", 6000, false, newStore);
//
//        List<Menu> menuList = new ArrayList<>();
//        menuList.add(newMenu1);
//        menuList.add(newMenu2);
//
//        given(menuRepository.findAllByStoreId(storeId)).willReturn(menuList);
//
//        // menuIdList 생성
//        List<Long> menuIdList = new ArrayList<>();
//
//        for(Menu menu : menuList){
//            menuIdList.add(menu.getId());
//        }
//
//        // when
//        for(OrderSaveRequestDto menu : menus){
//            BaseException exception = assertThat(BaseException.class, () -> {
//
//            });
//        }
//    }

    @Test
    public void 주문_요청_테스트(){
        // given & when
        Long userId = 1L;
        UserRole userRole = UserRole.USER;
        Long storeId = 1L;

        // menus 생성
        OrderSaveRequestDto orderSaveRequestDto = new OrderSaveRequestDto();

        ReflectionTestUtils.setField(orderSaveRequestDto, "menuId", 1L);
        ReflectionTestUtils.setField(orderSaveRequestDto, "count", 1);

        List<OrderSaveRequestDto> menus = new ArrayList<>();
        menus.add(orderSaveRequestDto);

        // menu 생성
        User newUser = new User(userId, "홍길동", "ijieun@gmail.com", "Password123@", "010-1234-1234", "서울특별시 마장동 388-12 204호", userRole, false);
        Store newStore = new Store(storeId, "동해반점", "서울특별시 마장동 388-12", "010-0000-0000", StoreCategory.CHINESE, 5000, LocalTime.of(9, 0, 0), LocalTime.of(22, 0, 0), 5.0, false, null, newUser);
        Menu newMenu = new Menu(1L, "짜장면", 5000, false, newStore);

        // totalPrice 계산
        Integer totalPrice = 0;

        for(OrderSaveRequestDto menu : menus){
            given(menuRepository.findByIdOrElseThrow(menu.getMenuId())).willReturn(newMenu);

            Menu findMenu = menuRepository.findByIdOrElseThrow(menu.getMenuId());

            totalPrice += findMenu.getPrice() * menu.getCount();
        }

        // order 생성
        given(userRepository.findByIdOrElseThrow(userId)).willReturn(newUser);
        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(newStore);

        User findUser = userRepository.findByIdOrElseThrow(userId);
        Store findStore = storeRepository.findByIdOrElseThrow(storeId);

        Order newOrder = new Order(1L, findUser, findStore, false, Status.PENDING, totalPrice);

        given(orderRepository.save(any())).willReturn(newOrder);

        Order savedOrder = orderRepository.save(newOrder);

        // newOrderItem 생성
        for(OrderSaveRequestDto menu : menus){
            given(menuRepository.findByIdOrElseThrow(menu.getMenuId())).willReturn(newMenu);

            Menu findMenu = menuRepository.findByIdOrElseThrow(menu.getMenuId());

            OrderItem newOrderItem = new OrderItem(newOrder, findMenu, menu.getCount());

            given(orderItemRepository.save(any())).willReturn(newOrderItem);

            OrderItem savedOrderItem = orderItemRepository.save(newOrderItem);

            // then
            assertThat(newOrderItem).isEqualTo(savedOrderItem);
        }

        // then
        assertThat(newOrder).isEqualTo(savedOrder);
    }
}






















