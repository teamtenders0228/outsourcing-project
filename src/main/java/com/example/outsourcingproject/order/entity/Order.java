package com.example.outsourcingproject.order.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.order.enums.Status;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private Boolean accepted;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long totalPrice;

    public Order(User user, Store store, Boolean accepted, Status status, Long totalPrice){
        this.user = user;
        this.store = store;
        this.accepted = accepted;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
