package com.example.outsourcingproject.order.service;

import com.example.outsourcingproject.order.dto.request.OrderSaveRequestDto;
import com.example.outsourcingproject.order.dto.response.OrderAcceptResponseDto;
import com.example.outsourcingproject.order.dto.response.OrderRejectResponseDto;
import com.example.outsourcingproject.order.dto.response.OrderStatusResponseDto;
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
}
