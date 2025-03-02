package com.example.outsourcingproject.order.dto.request;

import com.example.outsourcingproject.order.enums.Status;
import lombok.Getter;

@Getter
public class OrderStatusRequestDto {

    private Status status;
}
