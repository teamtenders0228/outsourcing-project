package com.example.outsourcingproject.domain.store.service;

import com.example.outsourcingproject.domain.store.dto.request.StoreCreateRequestDto;
import com.example.outsourcingproject.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    @Transactional
    public StoreResponseDto createStore(StoreCreateRequestDto dto) {
        // 유저 등급 검증
        // 유저 검증
        // 가게 개수 검증
        // 가게 카테고리
        StoreCategory category = StoreCategory.of(dto.getCategory());

        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .category(category)
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .build();

        Store savedStore = storeRepository.save(store);
        return StoreResponseDto.fromEntity(savedStore);
    }
}
