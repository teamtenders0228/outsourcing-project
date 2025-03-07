package com.example.outsourcingproject.domain.store.service;

import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.order.entity.Order;
import com.example.outsourcingproject.domain.order.enums.Status;
import com.example.outsourcingproject.domain.review.entity.Review;
import com.example.outsourcingproject.domain.review.repository.ReviewRepository;
import com.example.outsourcingproject.domain.store.dto.request.StoreDeleteRequestDto;
import com.example.outsourcingproject.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcingproject.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreWithMenuResponseDto;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.enums.UserRole;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
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
    private User ownerUser;
    private Store store;
    private StoreSaveRequestDto storeDto;
    private StoreUpdateRequestDto storeUpdateRequestDto;

    @BeforeEach
    void setup(){
       ownerUser = new User(
                "홍길동",
                "Hong-GilDong@test.com",
                "1Q2w3e4r!",
                "010-1234-5678",
                "경기도 양주시 옥정동",
                UserRole.OWNER
        );
        ReflectionTestUtils.setField(ownerUser, "id", 1L);

        store = Store.builder()
                .id(1L)
                .storeName("가게1")
                .address("서울시 강남구")
                .phone("010-1234-5678")
                .category(StoreCategory.KOREAN)
                .minPrice(10000)
                .openTime(LocalTime.of(10, 0))
                .closeTime(LocalTime.of(22, 0))
                .user(ownerUser)
                .build();

        storeDto = StoreSaveRequestDto.builder()
                .storeName("테스트운영 가게")
                .address("서울시 성동구")
                .phone("010-1234-5678")
                .category("KOREAN")
                .minPrice(10000)
                .openTime(LocalTime.of(11, 0))
                .closeTime(LocalTime.of(23, 0))
                .build();

        storeUpdateRequestDto = StoreUpdateRequestDto.builder()
                .storeName("새로운 가게")
                .address("서울시 성동구")
                .phone("010-2222-3333")
                .category(StoreCategory.CHINESE)
                .minPrice(12000)
                .openTime(LocalTime.of(9, 0))
                .closeTime(LocalTime.of(21, 0))
                .build();
    }

    @Test
    void saveStore_가게가_성공적으로_등록() {
        Long authUserId = 1L;

        Store newStore = Store.builder()
                .storeName(storeDto.getStoreName())
                .address(storeDto.getAddress())
                .phone(storeDto.getPhone())
                .category(StoreCategory.KOREAN)
                .minPrice(storeDto.getMinPrice())
                .openTime(storeDto.getOpenTime())
                .closeTime(storeDto.getCloseTime())
                .user(ownerUser)
                .build();

        when(userRepository.findById(authUserId)).thenReturn(Optional.of(ownerUser));
        when(storeRepository.countByUserId(authUserId)).thenReturn(0);
        when(storeRepository.existsByStoreName(storeDto.getStoreName())).thenReturn(false);
        when(storeRepository.save(any(Store.class))).thenReturn(newStore);

        StoreResponseDto result = storeOwnerService.saveStore(authUserId, storeDto);

        assertNotNull(result);
        assertEquals(storeDto.getStoreName(), result.getStoreName());
        assertEquals(storeDto.getAddress(), result.getAddress());

        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    void saveStore_가게가_3개_이상이면_예외발생() {
        Long authUserId = 1L;

        when(userRepository.findById(authUserId)).thenReturn(Optional.of(ownerUser));
        when(storeRepository.countByUserId(authUserId)).thenReturn(3);

        BaseException exception = assertThrows(BaseException.class, () -> storeOwnerService.saveStore(authUserId, storeDto));
        assertEquals(ErrorCode.EXCEED_STORE_LIMIT, exception.getErrorCode());
    }

    @Test
    void saveStore_중복된_이름이면_예외발생() {
        Long authUserId = 1L;

        when(userRepository.findById(authUserId)).thenReturn(Optional.of(ownerUser));
        when(storeRepository.countByUserId(authUserId)).thenReturn(0);
        when(storeRepository.existsByStoreName(storeDto.getStoreName())).thenReturn(true);

        BaseException exception = assertThrows(BaseException.class, () -> storeOwnerService.saveStore(authUserId, storeDto));
        assertEquals(ErrorCode.CONFLICT_STORE_NAME, exception.getErrorCode());
    }

    @Test
    void findAllStores_성공적으로_조회된다() {
        // Given
        Long authUserId = 1L;

        List<Store> stores = List.of(
                store,
                Store.builder()
                        .storeName("가게2")
                        .address("서울시 성동구")
                        .phone("010-5678-1234")
                        .category(StoreCategory.CHINESE)
                        .minPrice(15000)
                        .openTime(LocalTime.of(10, 0))
                        .closeTime(LocalTime.of(23, 0))
                        .rating(4.2)
                        .deleteAt(null)
                        .user(ownerUser)
                        .build()
        );
        when(storeRepository.findAllByUserId(authUserId)).thenReturn(stores);

        // When
        List<StoreResponseDto> result = storeOwnerService.findAllStores(authUserId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("가게1", result.get(0).getStoreName());
        assertEquals("서울시 강남구",result.get(0).getAddress());
        assertEquals(String.valueOf(StoreCategory.KOREAN), result.get(0).getCategory());
        assertEquals(10000, result.get(0).getMinPrice());

        assertEquals("가게2", result.get(1).getStoreName());
        assertEquals("서울시 성동구",result.get(1).getAddress());
        assertEquals(String.valueOf(StoreCategory.CHINESE), result.get(1).getCategory());
        assertEquals(15000, result.get(1).getMinPrice());

        verify(storeRepository, times(1)).findAllByUserId(authUserId);
    }

    @Test
    void findAllStores_조회되는_가게가_없으면_빈_리스트를_반환(){
        // Given
        Long authUserId = 1L;

        when(storeRepository.findAllByUserId(authUserId)).thenReturn(Collections.emptyList());

        List<StoreResponseDto> result = storeOwnerService.findAllStores(authUserId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(storeRepository, times(1)).findAllByUserId(authUserId);
    }

    @Test
    void findStoreById_존재하는_가게_및_메뉴_조회_성공() {
        // Given
        Long storeId = 1L;
        Long authUserId = 1L;

        Store store = Store.builder()
                .storeName("테스트 가게")
                .address("서울시 강남구")
                .phone("010-1234-5678")
                .category(StoreCategory.KOREAN)
                .minPrice(10000)
                .openTime(LocalTime.of(9, 0))
                .closeTime(LocalTime.of(22, 0))
                .rating(4.5)
                .user(ownerUser)
                .build();

        List<Menu> menus = List.of(
                new Menu("김치찌개", 8000, store),
                new Menu("된장찌개", 8500, store)
        );

        when(storeRepository.findByIdAndUserId(storeId, authUserId)).thenReturn(Optional.of(store));
        when(menuRepository.findAllByStoreId(storeId)).thenReturn(menus);

        // When
        StoreWithMenuResponseDto result = storeOwnerService.findStoreById(storeId, authUserId);

        // Then
        assertNotNull(result);
        assertEquals("테스트 가게", result.getStoreName());
        assertEquals(2, result.getMenus().size());
        assertEquals("김치찌개", result.getMenus().get(0).getMenuName());
        assertEquals("된장찌개", result.getMenus().get(1).getMenuName());

        verify(storeRepository, times(1)).findByIdAndUserId(storeId, authUserId);
        verify(menuRepository, times(1)).findAllByStoreId(storeId);
    }

    @Test
    void findStoreById_존재하지_않는_가게_조회시_예외발생() {
        // Given
        Long storeId = 999L;
        Long authUserId = 1L;
        when(storeRepository.findByIdAndUserId(storeId, authUserId)).thenReturn(Optional.empty());

        // When & Then
        BaseException exception = assertThrows(BaseException.class, () -> storeOwnerService.findStoreById(storeId, authUserId));
        assertEquals(ErrorCode.STORE_NOT_FOUND, exception.getErrorCode());

        verify(storeRepository, times(1)).findByIdAndUserId(storeId, authUserId);
    }

    @Test
    void updateStore_성공적으로_업데이트() {
        Long storeId = 1L;
        Long authUserId = 1L;


        when(storeRepository.getStoreById(storeId)).thenReturn(Optional.of(store));
        when(storeRepository.existsByStoreName(storeUpdateRequestDto.getStoreName())).thenReturn(false);
        // When
        StoreResponseDto result = storeOwnerService.updateStore(authUserId, storeId, storeUpdateRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(storeUpdateRequestDto.getStoreName(), store.getStoreName());
        assertEquals(storeUpdateRequestDto.getAddress(), store.getAddress());
        assertEquals(storeUpdateRequestDto.getPhone(), store.getPhone());
        assertEquals(storeUpdateRequestDto.getCategory(), store.getCategory());
        assertEquals(storeUpdateRequestDto.getMinPrice(), store.getMinPrice());
        assertEquals(storeUpdateRequestDto.getOpenTime(), store.getOpenTime());
        assertEquals(storeUpdateRequestDto.getCloseTime(), store.getCloseTime());

        verify(storeRepository, times(1)).getStoreById(storeId);
    }

    @Test
    void updateStore_일부_필드만_변경() {
        // Given
        Long storeId = 1L;
        Long authUserId = 1L;

        Store store = Store.builder()
                .id(storeId)
                .storeName("기존 가게")
                .address("서울시 강남구")
                .phone("010-1111-2222")
                .category(StoreCategory.KOREAN)
                .minPrice(10000)
                .openTime(LocalTime.of(10, 0))
                .closeTime(LocalTime.of(22, 0))
                .user(ownerUser)
                .build();

        StoreUpdateRequestDto partialUpdateDto = StoreUpdateRequestDto.builder()
                .storeName("기존 가게")
                .address("서울시 강남구")
                .phone("010-9999-9999") // 전화번호만 변경
                .category(StoreCategory.KOREAN)
                .minPrice(10000)
                .openTime(LocalTime.of(10, 0))
                .closeTime(LocalTime.of(22, 0))
                .build();

        when(storeRepository.getStoreById(storeId)).thenReturn(Optional.of(store));

        // When
        StoreResponseDto result = storeOwnerService.updateStore(authUserId, storeId, partialUpdateDto);

        // Then
        assertNotNull(result);
        assertEquals("010-9999-9999", store.getPhone()); // 변경된 값 확인

        verify(storeRepository, times(1)).getStoreById(storeId);
    }

    @Test
    void updateStore_가게가_존재하지_않으면_예외발생() {
        // Given
        Long storeId = 999L;
        Long authUserId = 1L;

        when(storeRepository.getStoreById(storeId)).thenReturn(Optional.empty());

        // When & Then
        BaseException exception = assertThrows(BaseException.class,
                () -> storeOwnerService.updateStore(authUserId, storeId, StoreUpdateRequestDto.builder().build()));

        assertEquals(ErrorCode.STORE_NOT_FOUND, exception.getErrorCode());
        verify(storeRepository, times(1)).getStoreById(storeId);
    }

    @Test
    void updateStore_소유자가_아니면_예외발생() {
        // Given
        Long storeId = 1L;
        Long anotherUserId = 2L;  // 다른 사용자 ID

        Store store = Store.builder()
                .id(storeId)
                .storeName("기존 가게")
                .address("서울시 강남구")
                .phone("010-1234-5678")
                .category(StoreCategory.KOREAN)
                .minPrice(10000)
                .openTime(LocalTime.of(10, 0))
                .closeTime(LocalTime.of(22, 0))
                .user(ownerUser)
                .build();

        when(storeRepository.getStoreById(storeId)).thenReturn(Optional.of(store));

        // When & Then
        BaseException exception = assertThrows(BaseException.class,
                () -> storeOwnerService.updateStore(anotherUserId, storeId, StoreUpdateRequestDto.builder().build()));

        assertEquals(ErrorCode.UNAUTHORIZED_STORE_ACCESS, exception.getErrorCode());
        verify(storeRepository, times(1)).getStoreById(storeId);
    }


    @Test
    void updateStore_가게_이름이_중복되면_예외발생() {
        // Given
        Long storeId = 1L;
        Long authUserId = 1L;

        Store store = Store.builder()
                .id(storeId)
                .storeName("기존 가게")
                .address("서울시 강남구")
                .phone("010-1234-5678")
                .category(StoreCategory.KOREAN)
                .minPrice(10000)
                .openTime(LocalTime.of(10, 0))
                .closeTime(LocalTime.of(22, 0))
                .user(ownerUser)
                .build();

        StoreUpdateRequestDto updateRequestDto = StoreUpdateRequestDto.builder()
                .storeName("기존 가게")
                .address("서울시 강남구")
                .phone("010-1234-5678")
                .category(StoreCategory.KOREAN)
                .minPrice(1000)
                .openTime(LocalTime.of(9, 0))
                .closeTime(LocalTime.of(21, 0))
                .build();

        when(storeRepository.getStoreById(storeId)).thenReturn(Optional.of(store));
        when(storeRepository.existsByStoreName(updateRequestDto.getStoreName())).thenReturn(true);

        // When & Then
        BaseException exception = assertThrows(BaseException.class,
                () -> storeOwnerService.updateStore(authUserId, storeId, updateRequestDto));

        assertEquals(ErrorCode.CONFLICT_STORE_NAME, exception.getErrorCode());

        verify(storeRepository, times(1)).getStoreById(storeId);
        verify(storeRepository, times(1)).existsByStoreName(updateRequestDto.getStoreName());
    }

    @Test
    void deleteStore_성공적으로_삭제() {
        // Given
        Long storeId = 1L;
        Long authUserId = 1L;
        String rawPassword = "1Q2w3e4r!";

        StoreDeleteRequestDto deleteRequestDto = new StoreDeleteRequestDto(rawPassword);

        when(storeRepository.getStoreById(storeId)).thenReturn(Optional.of(store));
        when(userRepository.findById(authUserId)).thenReturn(Optional.of(ownerUser));
        when(passwordEncoder.matches(deleteRequestDto.getPassword(), ownerUser.getPassword())).thenReturn(true);
        // When
        storeOwnerService.deleteStore(authUserId, storeId, deleteRequestDto);

        // Then
        verify(storeRepository, times(1)).getStoreById(storeId);
        verify(userRepository, times(1)).findById(authUserId);
        verify(passwordEncoder, times(1)).matches(deleteRequestDto.getPassword(), ownerUser.getPassword());

        assertNotNull(store.getDeleteAt());
    }

    @Test
    void deleteStore_소유자가_아니면_예외발생() {
        // Given
        Long storeId = 1L;
        Long anotherUserId = 2L; // 다른 사용자
        String rawPassword = "1Q2w3e4r!";

        StoreDeleteRequestDto deleteRequestDto = new StoreDeleteRequestDto(rawPassword);
        when(storeRepository.getStoreById(storeId)).thenReturn(Optional.of(store));

        // When & Then
        BaseException exception = assertThrows(BaseException.class,
                () -> storeOwnerService.deleteStore(anotherUserId, storeId,deleteRequestDto));

        assertEquals(ErrorCode.UNAUTHORIZED_STORE_ACCESS, exception.getErrorCode());
        verify(storeRepository, times(0)).delete(any(Store.class));
    }

    @Test
    void deleteStore_비밀번호_틀리면_예외발생() {
        // Given
        Long storeId = 1L;
        Long authUserId = 1L;
        String incorrectPassword = "4R3e2w1q!";

        StoreDeleteRequestDto deleteRequestDto = new StoreDeleteRequestDto(incorrectPassword);

        when(storeRepository.getStoreById(storeId)).thenReturn(Optional.of(store));
        when(userRepository.findById(authUserId)).thenReturn(Optional.of(ownerUser));
        when(passwordEncoder.matches(deleteRequestDto.getPassword(), ownerUser.getPassword())).thenReturn(false);

        // When & Then
        BaseException exception = assertThrows(BaseException.class,
                () -> storeOwnerService.deleteStore(authUserId, storeId, deleteRequestDto));

        assertEquals(ErrorCode.PASSWORD_MISMATCH, exception.getErrorCode());

        verify(storeRepository, times(1)).getStoreById(storeId);
        verify(userRepository, times(1)).findById(authUserId);
        verify(passwordEncoder, times(1)).matches(deleteRequestDto.getPassword(), ownerUser.getPassword());

        verify(storeRepository, never()).delete(store);
    }


    @Test
    void toggleStoreStatus_성공적으로_상태_변경() {
        // Given
        Long storeId = 1L;
        Long authUserId = 1L;

        when(storeRepository.getStoreById(storeId)).thenReturn(Optional.of(store));

        // When
        storeOwnerService.toggleStoreStatus(authUserId, storeId);

        // Then
        assertNotNull(store);
        verify(storeRepository, times(1)).getStoreById(storeId);
    }

    @Test
    void toggleStoreStatus_폐업된_가게는_예외발생() {
        // Given
        Long storeId = 1L;
        Long authUserId = 1L;

        Store closedStore = spy(store);

        // deleteAt가 null이 아닐때
        ReflectionTestUtils.setField(closedStore, "closedFlag", true);
        ReflectionTestUtils.setField(closedStore, "deleteAt", LocalDateTime.now());

        when(storeRepository.getStoreById(storeId)).thenReturn(Optional.of(closedStore));

        // When & Then
        BaseException exception = assertThrows(BaseException.class,
                () -> storeOwnerService.toggleStoreStatus(authUserId, storeId));

        assertEquals(ErrorCode.CANNOT_MODIFY_DELETED_STORE, exception.getErrorCode());

        verify(storeRepository, times(1)).getStoreById(storeId);
        verify(closedStore, never()).toggleStoreStatus();
    }


    @Test
    void toggleStoreStatus_소유자가_아니면_예외발생() {
        // Given
        Long storeId = 1L;
        Long anotherUserId = 2L;

        when(storeRepository.getStoreById(storeId)).thenReturn(Optional.of(store));

        // When & Then
        BaseException exception = assertThrows(BaseException.class,
                () -> storeOwnerService.toggleStoreStatus(anotherUserId, storeId));

        assertEquals(ErrorCode.UNAUTHORIZED_STORE_ACCESS, exception.getErrorCode());
    }

    @Test
    void updateRating_성공적으로_별점_업데이트() {
        // Given
        Long storeId = 1L;
        User customer = new User("홍길동", "gildong@naver.com", "password1!", "010-3333-5555", "서울시 성북구 장위동", UserRole.USER);
        Order order1 = new Order(customer, store, true, Status.COMPLETE, 15000);
        Order order2 = new Order(customer, store, true, Status.COMPLETE, 20000);
        Order order3 = new Order(customer, store, true, Status.COMPLETE, 25000);

        List<Review> reviews = List.of(
                new Review(order1, "좋아요", 5),
                new Review(order2, "괜찮아요", 4),
                new Review(order3, "그냥 그래요", 3)
        );

        when(storeRepository.getStoreById(storeId)).thenReturn(Optional.of(store));
        when(reviewRepository.findByStoreId(storeId)).thenReturn(reviews);

        // When
        storeOwnerService.updateRating(storeId);

        // Then
        double expectedRating = Math.round((5 + 4 + 3) / 3.0 * 10.0) / 10.0;
        assertEquals(expectedRating, store.getRating());

        verify(storeRepository, times(1)).getStoreById(storeId);
        verify(reviewRepository, times(1)).findByStoreId(storeId);
    }
}