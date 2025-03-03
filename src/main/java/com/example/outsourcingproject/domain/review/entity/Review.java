package com.example.outsourcingproject.domain.review.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comments;

    @Column(nullable = false)
    private Integer rate;

    public Review(String comments, Integer rate) {
        this.comments = comments;
        this.rate = rate;
    }

    // comments만 수정
    public void updateComments(String comments) {
        this.comments = comments;
    }

    // rate만 수정
    public void updateRate(Integer rate) {
        this.rate = rate;
    }
}
