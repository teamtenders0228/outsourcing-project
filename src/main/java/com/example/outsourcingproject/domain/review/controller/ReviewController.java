package com.example.outsourcingproject.domain.review.controller;

import com.example.outsourcingproject.domain.review.dto.request.ReviewSaveRequestDto;
import com.example.outsourcingproject.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.outsourcingproject.domain.review.dto.response.ReviewResponseDto;
import com.example.outsourcingproject.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponseDto> saveReview(@RequestBody ReviewSaveRequestDto dto){
        log.info("리뷰 생성 성공");
        return ResponseEntity.ok(reviewService.saveReview(dto));
    }

    // 리뷰 다건 조회 (확인 후 삭제)
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getAllReview(){
        return ResponseEntity.ok(reviewService.findAll());
    }
    // 리뷰 조회 (최신순)
    // 리뷰 조회 (별점순)
    // 리뷰 수정
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequestDto dto
    ){
        log.info("리뷰 수정 성공");
        return ResponseEntity.ok(reviewService.updateReview(reviewId, dto));
    }
    // 리뷰 삭제
}
