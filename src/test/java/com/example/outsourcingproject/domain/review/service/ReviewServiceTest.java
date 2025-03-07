package com.example.outsourcingproject.domain.review.service;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.order.enums.Status;
import com.example.outsourcingproject.domain.order.repository.OrderRepository;
import com.example.outsourcingproject.domain.review.dto.request.ReviewSaveRequestDto;
import com.example.outsourcingproject.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.outsourcingproject.domain.review.dto.response.ReviewResponseDto;
import com.example.outsourcingproject.domain.review.entity.Review;
import com.example.outsourcingproject.domain.review.repository.ReviewRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import com.example.outsourcingproject.domain.store.service.StoreOwnerService;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    ReviewRepository reviewRepository;
    @Mock
    StoreOwnerService storeOwnerService;
    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    ReviewService reviewService;

    private User user;
    private AuthUser authUser;
    private User storeOwner;
    private Store store;
    private Order order;

    @BeforeEach
    void setup(){
        // 사용자 생성
        user = new User("John Doe", "john@email.com", "rawpassword1@", "010-1234-5678",
                "서울특별시 노원구", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);
        authUser = new AuthUser(1L, "john@email.com", UserRole.USER);

        // 가게 사장 생성
        storeOwner = new User("Amy", "amy@example.com",
                "password1!", "010-1111-2222",
                "서울특별시 노원구 상계1동", UserRole.OWNER);
        ReflectionTestUtils.setField(storeOwner, "id", 2L);

        store = new Store(1L,"GoodPlace","서울특별시 노원구 상계동 590",
                "02-123-4567",StoreCategory.WESTERN, 10000,
                LocalTime.parse("10:00:00"),LocalTime.parse("22:00:00"),
                0.0,false,null, storeOwner);

//        Menu menu1 = new Menu("pasta", 15000, store);
//        Menu menu2 = new Menu("salad", 10000, store);
//        Menu menu3 = new Menu("steak", 20000, store);

        order = new Order(user, store, true, Status.COMPLETE, 35000);
        ReflectionTestUtils.setField(order, "id", 1L);
    }

    @Test
    void 리뷰_저장에_성공한다() {
        // given
        ReviewSaveRequestDto requestDto = new ReviewSaveRequestDto("It's delicious!", 5);
        Review review = new Review(order, requestDto.getComments(), requestDto.getRate());

        given(orderRepository.findByIdOrElseThrow(anyLong())).willReturn(order);

        ReflectionTestUtils.setField(review, "id", 1L);
        given(reviewRepository.save(any())).willReturn(review);

        //when
        ReviewResponseDto responseDto = reviewService.saveReview(authUser, order.getId(), requestDto);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getComments()).isEqualTo("It's delicious!");
        assertThat(responseDto.getRate()).isEqualTo(5);
    }

    @Test
    void 해당_이메일로_이미_리뷰가_존재한다() {
        // given
        ReviewSaveRequestDto requestDto = new ReviewSaveRequestDto("It's delicious!", 5);

        given(reviewRepository.existsByOrderIdAndUserId(order.getId(), authUser.getId())).willReturn(true);

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> {
            reviewService.saveReview(authUser, order.getId(), requestDto);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_REVIEW);
    }

    @Test
    void 가게_id로_리뷰를_조회한다() {
        // given
        Long storeId = 1L;
        Review review = new Review(order, "It's delicious!", 5);
        ReflectionTestUtils.setField(review, "id", 1L);

        // 페이지 객체가 필요
        List<Review> reviews = List.of(review);
        Page<Review> reviewPage = new PageImpl<>(reviews, PageRequest.of(0, 10), reviews.size());  // Page 객체 생성

        //given(reviewRepository.save(any())).willReturn(review);
        given(reviewRepository.findByStoreIdWithPagination(eq(storeId), any(Pageable.class))).willReturn(reviewPage);

        // when
        Page<ReviewResponseDto> responseDtos = reviewService.findReviewsByStoreId(storeId, 1, 10);

        // then
        assertThat(responseDtos).isNotNull();
        assertThat(responseDtos.getTotalElements()).isEqualTo(1);
        assertThat(responseDtos.getContent().get(0).getComments()).isEqualTo("It's delicious!");
    }

    @Test
    void findReviewsByUserId() {
        // given
        Review review = new Review(order, "It's delicious!", 5);
        ReflectionTestUtils.setField(review, "id", 1L);

        List<Review> reviews = List.of(review);
        Page<Review> reviewPage = new PageImpl<>(reviews, PageRequest.of(0, 10), reviews.size());

        given(reviewRepository.findByUserId(eq(authUser.getId()), any(Pageable.class))).willReturn(reviewPage);

        // when
        Page<ReviewResponseDto> responseDtos = reviewService.findReviewsByUserId(authUser, 1, 10);

        // then
        assertThat(responseDtos).isNotNull();
        assertThat(responseDtos.getTotalElements()).isEqualTo(1);
        assertThat(responseDtos.getContent().get(0).getComments()).isEqualTo("It's delicious!");
    }

    @Test
    void 리뷰_수정에_성공한다() {
        // given
        Long reviewId = 1L;

        Review review = new Review(order, "It's delicious!", 5);
        ReflectionTestUtils.setField(review, "id", 1L);

        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto("It's good!", 4);
        given(reviewRepository.findById(eq(reviewId))).willReturn(Optional.of(review));

        doNothing().when(storeOwnerService).updateRating(anyLong());

        // when
        ReviewResponseDto responseDto = reviewService.updateReview(authUser, reviewId, requestDto);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getComments()).isEqualTo("It's good!");
        assertThat(responseDto.getRate()).isEqualTo(4);
    }

    @Test
    void 본인이_작성한_리뷰가_아니라면_수정할_수_없다(){
        // given
        Long reviewId = 1L;
        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto("It's good!", 4);

        Review review = new Review(order, "It's delicious", 5);
        ReflectionTestUtils.setField(review, "id", reviewId);

        // 다른 유저가 수정 시도
        User another = new User("The other", "other@email.com", "password2@", "010-7890-1234",
                "서울특별시 강남구", UserRole.USER);
        AuthUser anotherAuthUser = new AuthUser(2L, "other@email.com", UserRole.USER);
        ReflectionTestUtils.setField(authUser, "id", 2L);

        given(reviewRepository.findById(eq(reviewId))).willReturn(Optional.of(review));

        // when & then
        BaseException exception = assertThrows(BaseException.class, () ->
                reviewService.updateReview(anotherAuthUser, reviewId, requestDto));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED_REVIEW_ACCESS);

    }

    @Test
    void 리뷰_삭제에_성공한다() {
        // given
        Long reviewId = 1L;

        Review review = new Review(order, "It's delicious!", 5);
        ReflectionTestUtils.setField(review, "id", 1L);

        given(reviewRepository.findById(eq(reviewId))).willReturn(Optional.of(review));
        doNothing().when(reviewRepository).delete(any());
        doNothing().when(storeOwnerService).updateRating(anyLong());

        // when
        reviewService.deleteReview(authUser, reviewId);

        // then
        verify(reviewRepository, times(1)).delete(review);
        verify(storeOwnerService, times(1)).updateRating(review.getOrder().getStore().getId());
    }

    @Test
    void 사장의_리뷰_삭제는_실패한다(){
        // given
        Long reviewId = 1L;

        Review review = new Review(order, "It's delicious!", 5);
        ReflectionTestUtils.setField(review, "id", 1L);

        AuthUser owner = new AuthUser(2L, "amy@example.com", UserRole.OWNER);

        given(reviewRepository.findById(eq(reviewId))).willReturn(Optional.of(review));

        // when & then
        BaseException exception = assertThrows(BaseException.class, () ->
                reviewService.deleteReview(owner, reviewId));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_USER_ROLE);
    }
}