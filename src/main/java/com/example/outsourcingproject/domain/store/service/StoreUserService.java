package com.example.outsourcingproject.domain.store.service;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreUserService {

    private final StoreRepository storeRepository;
    public List<StoreResponseDto> getAllStores() {
        List<Store> stores = storeRepository.findAllOpenStores();
        return stores.stream()
                .map(StoreResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public StoreResponseDto getStoreById(Long storeId) {
        Store store = storeRepository.getOpenStoreById(storeId)
                .orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND, null));

        return StoreResponseDto.fromEntity(store);
    }
}
