package com.example.outsourcingproject.domain.order.dto.request;

import com.example.outsourcingproject.domain.order.enums.Status;
import lombok.Getter;

@Getter
public class OrderStatusRequestDto {

    private Status status;
}
