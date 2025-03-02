package com.example.outsourcingproject.order.service;

import com.example.outsourcingproject.order.dto.request.OrderSaveRequestDto;
import com.example.outsourcingproject.order.dto.response.OrderAcceptResponseDto;
import com.example.outsourcingproject.order.entity.Order;
import com.example.outsourcingproject.order.enums.Status;
import com.example.outsourcingproject.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // 주문 요청 - (일반 유저만 가능)
//    public void orderSave(OrderSaveRequestDto.MenuRequest menus) {
//        menus.
//    }

    // 수락 - (가게사장님 만 가능)
    @Transactional
    public OrderAcceptResponseDto orderAccept(Long orderId) {
        Order findOrder = orderRepository.findByIdOrElseThrow(orderId);

        findOrder.setStatus(Status.ACCEPT);
        findOrder.setAccepted(true);

        return new OrderAcceptResponseDto("주문을 수락하였습니다.");
    }
}
