package com.example.outsourcingproject.domain.review.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSaveRequestDto {
    @NotBlank(message = "리뷰내용을 입력하세요")
    @Size(min = 1, message = "리뷰내용을 입력하세요")
    private String comments;

    @NotNull(message = "rate를 입력하세요.")
    @Min(value = 1, message = "별점은 1이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5이하여야 합니다.")
    private Integer rate;
}
