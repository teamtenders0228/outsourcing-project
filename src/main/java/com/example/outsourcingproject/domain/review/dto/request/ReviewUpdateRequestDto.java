package com.example.outsourcingproject.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequestDto {

    private String comments;

    @Min(value = 1, message = "별점은 1이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5이하여야 합니다.")
    private int rate;
}
