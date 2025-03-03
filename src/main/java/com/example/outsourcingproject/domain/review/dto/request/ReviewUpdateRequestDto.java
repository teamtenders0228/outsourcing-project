package com.example.outsourcingproject.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewUpdateRequestDto {

    private String comments;

    @Size(min = 1, max = 5, message = "별점은 1~5 사이만 입력 가능합니다.")
    private int rate;
}
