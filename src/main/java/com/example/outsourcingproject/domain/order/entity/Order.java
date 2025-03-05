package com.example.outsourcingproject.domain.order.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.order.enums.Status;
import com.example.outsourcingproject.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stores_id")
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
