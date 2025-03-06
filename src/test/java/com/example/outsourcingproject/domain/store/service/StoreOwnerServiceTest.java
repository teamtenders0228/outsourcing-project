package com.example.outsourcingproject.domain.store.service;

import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.review.repository.ReviewRepository;
import com.example.outsourcingproject.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRole;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreOwnerServiceTest {
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private StoreOwnerService storeOwnerService;

    @Test
    void saveStore_가게가_성공적으로_등록된다() {
        Long authUserId = 1L;

        StoreSaveRequestDto dto = StoreSaveRequestDto.builder()
                .storeName("테스트운영 가게")
                .address("서울시 성동구 금호동")
                .phone("010-1234-5678")
                .category("KOREAN")
                .minPrice(10000)
                .openTime(LocalTime.of(11, 0))
                .closeTime(LocalTime.of(23, 0))
                .build();

        User user = new User(
                "홍길동",
                "Hong-GilDong@test.com",
                "1Q2w3e4r!",
                "010-1234-5678",
                "경기도 양주시 옥정동",
                UserRole.ADMIN);

        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .category(StoreCategory.KOREAN)
                .minPrice(dto.getMinPrice())
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .rating(0.0)
                .user(user)
                .build();
        when(userRepository.findById(authUserId)).thenReturn(Optional.of(user));
        when(storeRepository.countByUserId(authUserId)).thenReturn(0);
        when(storeRepository.existsByStoreName(dto.getStoreName())).thenReturn(false);
        when(storeRepository.save(any(Store.class))).thenReturn(store);

        StoreResponseDto result = storeOwnerService.saveStore(authUserId, dto);

        // Then
        assertNotNull(result);
        assertEquals(dto.getStoreName(), result.getStoreName());
        assertEquals(dto.getAddress(), result.getAddress());
        verify(storeRepository, times(1)).save(any(Store.class));

    }

    @Test
    void saveStore_가게가_3개_이상이면_예외발생(){
        // Given
        Long authUserId = 1L;
        StoreSaveRequestDto dto = StoreSaveRequestDto.builder()
                .storeName("테스트 가게")
                .address("서울시 성동구")
                .phone("010-1234-5678")
                .category("KOREAN")
                .minPrice(10000)
                .openTime(LocalTime.of(11, 0))
                .closeTime(LocalTime.of(23, 0))
                .build();

        User user = new User("홍길동", "test@email.com", "password!", "010-1234-5678", "서울", UserRole.ADMIN);

        when(userRepository.findById(authUserId)).thenReturn(Optional.of(user));
        when(storeRepository.countByUserId(authUserId)).thenReturn(3); // 가게 개수 3개 이상

        // When & Then
        assertThrows(BaseException.class, () -> storeOwnerService.saveStore(authUserId, dto));
    }

    @Test
    void findAllStores() {
    }

    @Test
    void findStoreById() {
    }

    @Test
    void updateStore() {
    }

    @Test
    void deleteStore() {
    }

    @Test
    void toggleStoreStatus() {
    }

    @Test
    void updateRating() {
    }
}