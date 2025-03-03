package com.example.outsourcingproject.domain.review.controller;

import com.example.outsourcingproject.domain.review.dto.request.ReviewSaveRequestDto;
import com.example.outsourcingproject.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.outsourcingproject.domain.review.dto.response.ReviewResponseDto;
import com.example.outsourcingproject.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponseDto> saveReview(
            @Valid @RequestBody ReviewSaveRequestDto dto){
        log.info("리뷰 생성 성공");
        return ResponseEntity.ok(reviewService.saveReview(dto));
    }

    // 리뷰 다건 조회 (확인 후 삭제)
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getAllReview(){
        return ResponseEntity.ok(reviewService.findAll());
    }

    // 리뷰 조회 (최신순)
    @GetMapping("/reviews/sorted-by-createdDate")
    public ResponseEntity<Page<ReviewResponseDto>> getReriewsSortedByCreateAt(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        log.info("생성일 기준 리뷰 전체 조회");
        return ResponseEntity.ok(reviewService.findReriewsSortedByCreateAt(page, size));
    }

    // 리뷰 조회 (별점범위)
    @GetMapping("/reviews/sorted-by-rate")
    public ResponseEntity<Page<ReviewResponseDto>> getReriewsSortedByRateRange(
            @Param("minRate") int minRate,
            @Param("maxRate") int maxRate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        log.info("별점범위 리뷰조회");
        return ResponseEntity.ok(reviewService.findReriewsSortedByRateRange(minRate, maxRate, page, size));
    }

    // 리뷰 수정
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequestDto dto
    ){
        log.info("리뷰 수정 성공");
        return ResponseEntity.ok(reviewService.updateReview(reviewId, dto));
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
    }
}
