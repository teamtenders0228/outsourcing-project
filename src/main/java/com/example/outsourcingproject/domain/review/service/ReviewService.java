package com.example.outsourcingproject.domain.review.service;

import com.example.outsourcingproject.domain.review.dto.request.ReviewSaveRequestDto;
import com.example.outsourcingproject.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.outsourcingproject.domain.review.dto.response.ReviewResponseDto;
import com.example.outsourcingproject.domain.review.entity.Review;
import com.example.outsourcingproject.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
    // 리뷰 조회 (별점순)

    // 리뷰 수정
    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto dto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new RuntimeException("리뷰가 없습니다.")
        );

        review.update(dto.getComments(), dto.getRate());

        return new ReviewResponseDto(
                review.getId(),
                review.getComments(),
                review.getRate()
        );
    }


    // 리뷰 삭제
}
