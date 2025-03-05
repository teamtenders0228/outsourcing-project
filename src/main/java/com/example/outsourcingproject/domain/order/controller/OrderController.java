package com.example.outsourcingproject.domain.order.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.order.dto.request.OrderSaveRequestDto;
import com.example.outsourcingproject.domain.order.dto.request.OrderStatusRequestDto;
import com.example.outsourcingproject.domain.order.dto.response.*;
import com.example.outsourcingproject.domain.order.service.OrderService;
import jakarta.validation.Valid;
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
    public ResponseEntity<OrderStatusResponseDto> orderSave(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody List<OrderSaveRequestDto> requestDto
    ){
        OrderStatusResponseDto responseDto = orderService.orderSave(authUser.getId(), authUser.getUserRole(), storeId, requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 수락 - (가게사장님 만 가능)
    @PatchMapping("/accept/{orderId}")
    public ResponseEntity<OrderStatusResponseDto> orderAccept(
            @Auth AuthUser authUser,
            @PathVariable Long orderId
    ){
        OrderStatusResponseDto responseDto = orderService.orderAccept(authUser.getId(), orderId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 주문 상태 변경 - (가게사장님 만 가능)(거절 외 순차적으로 변경)
    @PatchMapping("/status/progress/{orderId}")
    public ResponseEntity<OrderStatusResponseDto> updateProgressStatus(
            @Auth AuthUser authUser,
            @PathVariable Long orderId
    ){
        OrderStatusResponseDto responseDto = orderService.updateProgressStatus(authUser.getId(), orderId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 주문 상태 변경 - (가게사장님 만 가능)(거절)
    @PatchMapping("/status/reject/{orderId}")
    public ResponseEntity<OrderStatusResponseDto> statusChangeReject(
            @Auth AuthUser authUser,
            @PathVariable Long orderId
    ){
        OrderStatusResponseDto responseDto = orderService.statusChangeReject(authUser.getId(), orderId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 사용자별 주문 내역 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserOrdersResponseDto>> findOrdersByUser(@PathVariable Long userId){
        List<UserOrdersResponseDto> responseDtoList = orderService.findOrdersByUser(userId);

        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }
}
