package com.example.outsourcingproject.domain.review.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Order order;

    @Column(nullable = false)
    private String comments;

    @Column(nullable = false)
    private int rate;

    public Review(Order order, String comments, int rate) {
        this.order = order;
        this.comments = comments;
        this.rate = rate;
    }

    public void updateComments(String comments, int rate) {
        this.comments = comments;
        this.rate = rate;
    }

}
