package com.example.outsourcingproject.domain.review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comments;

    @Column(nullable = false)
    private int rate;

    public Review(String comments, int rate) {
        this.comments = comments;
        this.rate = rate;
    }

    public void update(String comments, int rate) {
        this.comments = comments;
        this.rate = rate;
    }
}
