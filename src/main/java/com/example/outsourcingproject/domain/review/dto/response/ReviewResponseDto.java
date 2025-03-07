package com.example.outsourcingproject.domain.review.dto.response;

import com.example.outsourcingproject.domain.review.entity.Review;
import lombok.Getter;

@Getter
public class ReviewResponseDto {

    private final Long id;

    private final String comments;

    private final int rate;

    public ReviewResponseDto(Long id, String comments, int rate) {
        this.id = id;
        this.comments = comments;
        this.rate = rate;
    }

    // 조회
    public static ReviewResponseDto toDto(Review review){
        return new ReviewResponseDto(
                review.getId(),
                review.getComments(),
                review.getRate()
        );
    }
}
