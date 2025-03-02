package com.example.outsourcingproject.order.controller;

import com.example.outsourcingproject.order.dto.request.OrderSaveRequestDto;
import com.example.outsourcingproject.order.dto.request.OrderStatusRequestDto;
import com.example.outsourcingproject.order.dto.response.OrderAcceptResponseDto;
import com.example.outsourcingproject.order.dto.response.OrderRejectResponseDto;
import com.example.outsourcingproject.order.dto.response.OrderSaveResponseDto;
import com.example.outsourcingproject.order.dto.response.OrderStatusResponseDto;
import com.example.outsourcingproject.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 요청 - (일반 유저만 가능)
//    @PostMapping("/{store_id}")
//    public ResponseEntity<OrderSaveResponseDto> orderSave(@RequestBody OrderSaveRequestDto requestDto){
//        orderService.orderSave(requestDto.getMenus());
//    }

    // 수락 - (가게사장님 만 가능)
    @PatchMapping("/accept/{orderId}")
    public ResponseEntity<OrderAcceptResponseDto> orderAccept(@PathVariable Long orderId){
        OrderAcceptResponseDto responseDto = orderService.orderAccept(orderId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 취소(거절) - (가게사장님 만 가능)
    @PatchMapping("/reject/{orderId}")
    public ResponseEntity<OrderRejectResponseDto> orderReject(@PathVariable Long orderId){
        OrderRejectResponseDto responseDto = orderService.orderReject(orderId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 주문 상태 변경 - (가게사장님 만 가능)
    @PatchMapping("/status/{orderId}")
    public ResponseEntity<OrderStatusResponseDto> statusChange(
            @PathVariable Long orderId,
            @RequestBody OrderStatusRequestDto requestDto
    ){
        OrderStatusResponseDto responseDto = orderService.statusChange(orderId, requestDto.getStatus());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
