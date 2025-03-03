package com.example.outsourcingproject.order.controller;

import com.example.outsourcingproject.order.dto.request.OrderSaveRequestDto;
import com.example.outsourcingproject.order.dto.request.OrderStatusRequestDto;
import com.example.outsourcingproject.order.dto.response.OrderAcceptResponseDto;
import com.example.outsourcingproject.order.dto.response.OrderRejectResponseDto;
import com.example.outsourcingproject.order.dto.response.OrderSaveResponseDto;
import com.example.outsourcingproject.order.dto.response.OrderStatusResponseDto;
import com.example.outsourcingproject.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    // 주문 요청 - (일반 유저만 가능)
    @PostMapping("/{storeId}")
    public ResponseEntity<OrderSaveResponseDto> orderSave(
            @PathVariable Long storeId,
            @RequestBody List<OrderSaveRequestDto> requestDto
    ){
        OrderSaveResponseDto responseDto = orderService.orderSave(storeId, requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

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
