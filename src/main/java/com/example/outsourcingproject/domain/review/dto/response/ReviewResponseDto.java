package com.example.outsourcingproject.domain.review.dto.response;

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
}
