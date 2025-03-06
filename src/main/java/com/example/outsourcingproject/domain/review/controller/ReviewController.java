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
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("/{orderId}")
    public ResponseEntity<String> saveReview(
            @Auth AuthUser authUser,
            @PathVariable Long orderId,
            @Valid @RequestBody ReviewSaveRequestDto dto
    ){
        reviewService.saveReview(authUser, orderId, dto);

        return new ResponseEntity<>("message : 리뷰 등록이 완료되었습니다.",HttpStatus.OK);
    }

    // 리뷰 조회
    // 1. store 조회
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByStore(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10")int size
    ){

        return ResponseEntity.ok(reviewService.findReviewsByStoreId(storeId, page, size));
    }


    // 2. user(사용자) 조회
    @GetMapping("/myreviews")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByUser(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10")int size
    ) {
        return ResponseEntity.ok(reviewService.findReviewsByUserId(authUser, page, size));
    }

    // 리뷰 수정
    @PatchMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(
            @Auth AuthUser authUser,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequestDto dto
    ){
        reviewService.updateReview(authUser, reviewId, dto);
        return new ResponseEntity<>("message : 리뷰 수정이 완료되었습니다.",HttpStatus.OK);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @Auth AuthUser authUser,
            @PathVariable Long reviewId
    ){
        reviewService.deleteReview(authUser, reviewId);
        return new ResponseEntity<>("message : 리뷰 삭제가 완료되었습니다.",HttpStatus.OK);
    }
}
