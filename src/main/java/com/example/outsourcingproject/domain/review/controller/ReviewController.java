package com.example.outsourcingproject.domain.review.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
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
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> saveReview(
            @Auth AuthUser authUser,
            @Valid @RequestBody ReviewSaveRequestDto dto){
        reviewService.saveReview(authUser, dto);
        log.info("리뷰 생성 성공");
        return new ResponseEntity<>("message : 리뷰 등록이 완료되었습니다.",HttpStatus.OK);
    }

    // 리뷰 다건 조회 (확인 후 삭제)
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getAllReview(@Auth AuthUser authUser){
        return ResponseEntity.ok(reviewService.findAll(authUser));
    }

    // 리뷰 조회 (최신순)
    @GetMapping("/reviews/sorted-by-createdDate")
    public ResponseEntity<Page<ReviewResponseDto>> getReriewsSortedByCreateAt(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        log.info("생성일 기준 리뷰 전체 조회");
        return ResponseEntity.ok(reviewService.findReriewsSortedByCreateAt(authUser, page, size));
    }

    // 리뷰 조회 (별점범위)
    @GetMapping("/reviews/sorted-by-rate")
    public ResponseEntity<Page<ReviewResponseDto>> getReriewsSortedByRateRange(
            @Auth AuthUser authUser,
            @Param("minRate") int minRate,
            @Param("maxRate") int maxRate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        log.info("별점범위 리뷰조회");
        return ResponseEntity.ok(reviewService.findReriewsSortedByRateRange(authUser, minRate, maxRate, page, size));
    }

    // 리뷰 수정
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<String> updateReview(
            @Auth AuthUser authUser,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequestDto dto
    ){
        reviewService.updateReview(authUser, reviewId, dto);
        log.info("리뷰 수정 성공");
        return new ResponseEntity<>("message : 리뷰 수정이 완료되었습니다.",HttpStatus.OK);
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @Auth AuthUser authUser,
            @PathVariable Long reviewId
    ){
        reviewService.deleteReview(authUser, reviewId);
        return new ResponseEntity<>("message : 리뷰 삭제가 완료되었습니다.",HttpStatus.OK);
    }
}
