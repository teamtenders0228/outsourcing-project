package com.example.outsourcingproject.domain.review.service;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.review.dto.request.ReviewSaveRequestDto;
import com.example.outsourcingproject.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.outsourcingproject.domain.review.dto.response.ReviewResponseDto;
import com.example.outsourcingproject.domain.review.entity.Review;
import com.example.outsourcingproject.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 리뷰 등록
    @Transactional
    public ReviewResponseDto saveReview(ReviewSaveRequestDto dto) {
        Review review = new Review(dto.getComments(), dto.getRate());
        reviewRepository.save(review);

        log.info("리뷰 생성 성공");
        return new ReviewResponseDto(
                review.getId(),
                review.getComments(),
                review.getRate()
        );
    }

    // 리뷰 다건 조회 (확인 후 삭제)
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findAll() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(review -> new ReviewResponseDto(
                        review.getId(),
                        review.getComments(),
                        review.getRate()
                )).toList();
    }

    // 리뷰 조회 (최신순)
    public Page<ReviewResponseDto> findReriewsSortedByCreateAt(int page, int size) {
        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size, Sort.by("createdAt").descending());
        Page<Review> reviewPage = reviewRepository.findAllByCreatedAt(pageable);

        List<ReviewResponseDto> dtoList = reviewPage.getContent().stream()
                .map(ReviewResponseDto::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, reviewPage.getTotalElements());
    }

    // 리뷰 조회 (별점범위)
    public Page<ReviewResponseDto> findReriewsSortedByRateRange(int minRate, int maxRate, int page, int size) {
        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size);
        Page<Review> reviewPage = reviewRepository.findAllByRateRange(minRate, maxRate, pageable);

        List<ReviewResponseDto> dtoList = reviewPage.getContent().stream()
                .map(ReviewResponseDto::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, reviewPage.getTotalElements());
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto dto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new BaseException(ErrorCode.REVIEW_NOT_FOUND, null)
        );

        if(review.getComments() != null){
            review.updateComments(dto.getComments());
        }

        if(review.getRate() != null){
            review.updateRate(dto.getRate());
        }

        return new ReviewResponseDto(
                review.getId(),
                review.getComments(),
                review.getRate()
        );
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new BaseException(ErrorCode.REVIEW_NOT_FOUND, null)
        );

        reviewRepository.deleteById(reviewId);
    }
}
