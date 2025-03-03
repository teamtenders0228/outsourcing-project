package com.example.outsourcingproject.orderItem.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.menu.entity.Menu;
import com.example.outsourcingproject.order.entity.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_item")
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Integer count;

    public OrderItem (Order order, Menu menu, Integer count){
        this.order = order;
        this.menu = menu;
        this.count = count;
    }
}
