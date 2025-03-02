package com.example.outsourcingproject.order.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.order.enums.Status;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Setter
    private Boolean accepted;

    @Enumerated(EnumType.STRING)
    @Setter
    private Status status;

    private Integer totalPrice;

    public Order(User user, Store store, Boolean accepted, Status status, Integer totalPrice){
        this.user = user;
        this.store = store;
        this.accepted = accepted;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
