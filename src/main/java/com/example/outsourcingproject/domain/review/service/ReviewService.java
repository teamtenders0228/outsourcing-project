package com.example.outsourcingproject.domain.review.service;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.order.repository.OrderRepository;
import com.example.outsourcingproject.domain.review.dto.request.ReviewSaveRequestDto;
import com.example.outsourcingproject.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.outsourcingproject.domain.review.dto.response.ReviewResponseDto;
import com.example.outsourcingproject.domain.review.entity.Review;
import com.example.outsourcingproject.domain.review.repository.ReviewRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.store.service.StoreService;
import com.example.outsourcingproject.domain.user.entity.UserRole;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreService storeService;
    private final OrderRepository orderRepository;

    // 리뷰 등록
    @Transactional
    public ReviewResponseDto saveReview(AuthUser authUser, Long orderId, ReviewSaveRequestDto dto) {
        // USER 검증
        if (!authUser.getUserRole().equals(UserRole.USER)){
            throw new BaseException(ErrorCode.INVALID_USER_ROLE, null);
        }

        // 같은 유저로 요청했을때
        if (reviewRepository.existsByOrderIdAndUserId(orderId, authUser.getId())){
            throw new BaseException(ErrorCode.DUPLICATE_REVIEW, null);
        }

        Order order = orderRepository.findByIdOrElseThrow(orderId);

        Review review = new Review(order, dto.getComments(), dto.getRate());
        reviewRepository.save(review);

        // 가게 평점 update
        storeService.updateRating(order.getStore().getId());

        return new ReviewResponseDto(
                review.getId(),
                review.getComments(),
                review.getRate()
        );
    }

    @Transactional
    public Page<ReviewResponseDto> findReviewsByStoreId(Long storeId, int page, int size) {
        Pageable pageable = PageRequest.of((page > 0) ? page - 1 : 0, size, Sort.by("createdAt").descending());
        Page<Review> reviewPage = reviewRepository.findByStoreIdWithPagination(storeId, pageable);

        return reviewPage.map(review -> new ReviewResponseDto(
                review.getId(),
                review.getComments(),
                review.getRate()
        ));
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findReviewsByUserId(AuthUser authUser, int page, int size) {
        // 로그인된 사용자 검증
        if (!authUser.getUserRole().equals(UserRole.USER)) {
            throw new BaseException(ErrorCode.INVALID_USER_ROLE, null);
        }

        Pageable pageable = PageRequest.of((page > 0) ? page - 1 : 0, size, Sort.by("createdAt").descending());
        Page<Review> reviewPage = reviewRepository.findByUserId(authUser.getId(), pageable);

        return reviewPage.map(review -> new ReviewResponseDto(
                review.getId(),
                review.getComments(),
                review.getRate()
        ));
    }

    @Transactional
    public ReviewResponseDto updateReview(AuthUser authUser, Long reviewId, ReviewUpdateRequestDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(ErrorCode.REVIEW_NOT_FOUND, null));

        if (!review.getOrder().getUser().getId().equals(authUser.getId())) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_REVIEW_ACCESS, null);
        }

        review.updateComments(dto.getComments(), dto.getRate());

        // 가게 평점 업데이트
        storeService.updateRating(review.getOrder().getStore().getId());

        return new ReviewResponseDto(
                review.getId(),
                review.getComments(),
                review.getRate()
        );
    }

    @Transactional
    public void deleteReview(AuthUser authUser, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(ErrorCode.REVIEW_NOT_FOUND, null));

        // 로그인한 사용자가 해당 리뷰의 작성자인지 검증
        if (!review.getOrder().getUser().getId().equals(authUser.getId())) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_REVIEW_ACCESS, null);
        }

        reviewRepository.delete(review);

        // 가게 평점 업데이트
        storeService.updateRating(review.getOrder().getStore().getId());
    }
}
