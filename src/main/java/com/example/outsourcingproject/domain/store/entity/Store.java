package com.example.outsourcingproject.domain.store.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import com.example.outsourcingproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stores")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String storeName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreCategory category;

    @Column(nullable = false)
    private Integer minPrice;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(nullable = false)
    private boolean closedFlag;

    private LocalDateTime deleteAt;

    //유저 이름 컬럼 추가 필요
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public void updateStore(String storeName, String address, String phone, StoreCategory category,
                     Integer minPrice, LocalTime openTime, LocalTime closeTime){
        this.storeName = storeName;
        this.address = address;
        this.phone = phone;
        this.category = category;
        this.minPrice = minPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
    public void deleteStore() {
        this.closedFlag = false;
        this.deleteAt = LocalDateTime.now();
    }

    // 영업 시작, 종료 토글
   public void toggleStoreStatus() {
        this.closedFlag = !this.closedFlag;
   }

    // 테스트에 필요한 생성자
    public Store(String storeName, String address, String phone, StoreCategory category, Integer minPrice, LocalTime openTime, LocalTime closeTime, Double rating) {
        this.storeName = storeName;
        this.address = address;
        this.phone = phone;
        this.category = category;
        this.minPrice = minPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;

    public void updateRate(Double rating) {
        this.rating = rating;
    }
}