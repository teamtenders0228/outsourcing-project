package com.example.outsourcingproject.store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "store")
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String phone;

    private String category;

    private String openTime;

    private String closeTime;

    private BigDecimal rating;

    private boolean closedFlag;

    public Store(String name, String address, String phone, String category, String openTime, String closeTime, BigDecimal rating, boolean closedFlag){
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.category = category;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.rating = rating;
        this.closedFlag = closedFlag;
    }
}
