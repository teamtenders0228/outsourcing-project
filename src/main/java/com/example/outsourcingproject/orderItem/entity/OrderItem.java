package com.example.outsourcingproject.orderItem.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.menu.entity.Menu;
import com.example.outsourcingproject.order.entity.Order;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "order_item")
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Integer count;
}
