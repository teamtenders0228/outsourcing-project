package com.example.outsourcingproject.domain.store.service;

import com.example.outsourcingproject.common.encoder.PasswordEncoder;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.review.entity.Review;
import com.example.outsourcingproject.domain.review.repository.ReviewRepository;
import com.example.outsourcingproject.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcingproject.domain.store.dto.request.StoreDeleteRequestDto;
import com.example.outsourcingproject.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreWithMenuResponseDto;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.outsourcingproject.common.exception.ErrorCode.PASSWORD_MISMATCH;
import static com.example.outsourcingproject.common.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StoreOwnerService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewRepository reviewRepository;

    @Transactional
    public StoreResponseDto saveStore(Long authUserId, StoreSaveRequestDto dto) {
        // 유저 검증
        User user = userRepository.findById(authUserId).orElseThrow(() -> new BaseException(USER_NOT_FOUND, null));
        // 가게 개수 검증
        int storeCount = storeRepository.countByUserId(authUserId);
        if(storeCount >= 3){
            throw new BaseException(ErrorCode.EXCEED_STORE_LIMIT, String.valueOf(storeCount));
        }
        // 가게 이름 중복 검증
        if(storeRepository.existsByStoreName(dto.getStoreName())){
          throw new BaseException(ErrorCode.CONFLICT_STORE_NAME, dto.getStoreName());
        }
        // 가게 카테고리
        StoreCategory category = StoreCategory.of(dto.getCategory());

        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .category(category)
                .minPrice(dto.getMinPrice())
                .rating(0.0)
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .user(user)
                .build();

        Store savedStore = storeRepository.save(store);
        return StoreResponseDto.fromEntity(savedStore);
    }

    @Transactional(readOnly = true)
    public List<StoreResponseDto> findAllStores(Long authUserId) {
        List<Store> stores = storeRepository.findAllByUserId(authUserId);
        return stores.stream()
                .map(StoreResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoreWithMenuResponseDto findStoreById(Long storeId, Long authUserId) {
        List<Store> storeList = storeRepository.findByUser_Id(authUserId);
        List<Long> storeIdList = new ArrayList<>();

        for(Store store : storeList){
            storeIdList.add(store.getId());
        }

        if(!storeIdList.contains(storeId)) throw new BaseException(ErrorCode.UNAUTHORIZED_STORE_ACCESS, null);

        Store store = storeRepository.findByIdAndUserId(storeId, authUserId)
                .orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND, null));
        List<Menu> menus = menuRepository.findAllByStoreId(storeId);
        return new StoreWithMenuResponseDto(store, menus);
    }


    @Transactional
    public StoreResponseDto updateStore(Long authUserId, Long storeId, StoreUpdateRequestDto dto){
        Store store = storeRepository.getStoreById(storeId)
                .orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND, null));

        validateOwner(authUserId, store);

        // 가게 이름 중복 검증 (storeName이 변경될 경우만)
        if (dto.getStoreName() != null && storeRepository.existsByStoreName(dto.getStoreName())) {
            throw new BaseException(ErrorCode.CONFLICT_STORE_NAME, dto.getStoreName());
        }

        // DTO에서 null이 아닌 값만 기존 필드 값으로 대체
        store.updateStore(
                dto.getStoreName(),
                dto.getAddress(),
                dto.getPhone(),
                dto.getCategory(),
                dto.getMinPrice(),
                dto.getOpenTime(),
                dto.getCloseTime()
        );

        return StoreResponseDto.fromEntity(store);
    }

    @Transactional
    public void deleteStore(Long authUserId, Long storeId, StoreDeleteRequestDto dto) {
        Store store = storeRepository.getStoreById(storeId)
                .orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND, null));

        validateOwner(authUserId, store);

        User user = userRepository.findById(authUserId)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND, null));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            throw new BaseException(PASSWORD_MISMATCH, null);
        }

        store.deleteStore();
    }

    @Transactional
    public String toggleStoreStatus(Long authUserId, Long storeId) {
        Store store = storeRepository.getStoreById(storeId)
                .orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND, null));

        validateOwner(authUserId, store);

        if (store.getDeleteAt() != null) {
            throw new BaseException(ErrorCode.CANNOT_MODIFY_DELETED_STORE, null);
        }

        store.toggleStoreStatus();
        return store.isClosedFlag() ? "가게 영업을 시작했습니다." : "가게 영업을 종료했습니다.";
    }

    @Transactional
    public void updateRating(Long storeId) {
        Store store = storeRepository.getStoreById(storeId)
                .orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND, null));

        List<Review> reviews = reviewRepository.findByStoreId(storeId);

        if(reviews.isEmpty()) {
            store.updateRate(0.0);
        }
        int cnt = reviews.size();
        int total = reviews.stream().mapToInt(Review::getRate).sum();

        double newRating = Math.round((double) total / cnt * 10.0) / 10.0;

        store.updateRate(newRating);
    }

    private void validateOwner(Long authUserId, Store store) {
        if(!store.getUser().getId().equals(authUserId)) {
            throw new BaseException(ErrorCode.UNAUTHORIZED_STORE_ACCESS, null);
        }
    }
}
