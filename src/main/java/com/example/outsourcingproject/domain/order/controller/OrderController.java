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
    public ResponseEntity<String> orderSave(
            @Auth AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody List<OrderSaveRequestDto> requestDto
    ){
        orderService.orderSave(authUser.getId(), authUser.getUserRole(), storeId, requestDto);

        return new ResponseEntity<>("주문이 완료되었습니다.", HttpStatus.CREATED);
    }

    // 수락 - (가게사장님 만 가능)
    @PatchMapping("/accept/{orderId}")
    public ResponseEntity<String> orderAccept(
            @Auth AuthUser authUser,
            @PathVariable Long orderId
    ){
        orderService.orderAccept(authUser.getUserRole(), orderId);

        return new ResponseEntity<>("주문이 수락되었습니다.", HttpStatus.OK);
    }

    // 취소(거절) - (가게사장님 만 가능)
    @PatchMapping("/reject/{orderId}")
    public ResponseEntity<String> orderReject(
            @Auth AuthUser authUser,
            @PathVariable Long orderId
    ){
        orderService.orderReject(authUser.getUserRole(), orderId);

        return new ResponseEntity<>("주문이 거절되었습니다. 불편을 드려 죄송합니다.", HttpStatus.OK);
    }

    // 주문 상태 변경 - (가게사장님 만 가능)
    @PatchMapping("/status/{orderId}")
    public ResponseEntity<OrderStatusResponseDto> statusChange(
            @Auth AuthUser authUser,
            @PathVariable Long orderId,
            @RequestBody OrderStatusRequestDto requestDto
    ){
        OrderStatusResponseDto responseDto = orderService.statusChange(authUser.getUserRole(), orderId, requestDto.getStatus());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 사용자별 주문 내역 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserOrdersResponseDto>> findOrdersByUser(@PathVariable Long userId){
        List<UserOrdersResponseDto> responseDtoList = orderService.findOrdersByUser(userId);

        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }
}
