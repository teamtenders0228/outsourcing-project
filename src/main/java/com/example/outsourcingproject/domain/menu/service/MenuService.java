package com.example.outsourcingproject.domain.menu.service;

import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.menu.dto.requestDto.MenuSaveRequestDto;
import com.example.outsourcingproject.domain.menu.dto.requestDto.MenuUpdateRequestDto;
import com.example.outsourcingproject.domain.menu.dto.responseDto.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    // 메뉴 등록
    @Transactional
    public MenuResponseDto saveMenu(AuthUser authUser, MenuSaveRequestDto dto, Long storeId) {
        // OWNER 검증
        if(!authUser.getUserRole().equals(UserRole.OWNER)){
            throw new BaseException(ErrorCode.INVALID_USER_ROLE, null);
        }

        // Store 검증
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new RuntimeException("가게가 없습니다.")
        );

        Menu menu = new Menu(dto.getMenuName(), dto.getPrice(), store);
        menuRepository.save(menu);

        return new MenuResponseDto(
                menu.getId(),
                store.getId(),
                menu.getMenuName(),
                menu.priceToString()
        );
    }

    // 메뉴 전체 조회
    @Transactional(readOnly = true)
    public Page<MenuResponseDto> findAllMenu(AuthUser authUser, int page, int size, Long storeId) {
        // OWNER 검증
        if(!authUser.getUserRole().equals(UserRole.OWNER)){
            throw new BaseException(ErrorCode.INVALID_USER_ROLE, null);
        }

        // Store 검증
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new RuntimeException("가게가 없습니다.")
        );

        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size);
        Page<Menu> menuPage = menuRepository.findAllByStoreId(store.getId(), pageable);

        List<MenuResponseDto> dtoList = menuPage.getContent().stream()
                .map(MenuResponseDto::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, menuPage.getTotalElements());
    }

    // 메뉴 단건 조회
    @Transactional(readOnly = true)
    public MenuResponseDto findById(AuthUser authUser, Long menuId, Long storeId) {
        // OWNER 검증
        if(!authUser.getUserRole().equals(UserRole.OWNER)){
            throw new BaseException(ErrorCode.INVALID_USER_ROLE, null);
        }

        // Store 검증
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new RuntimeException("가게가 없습니다.")
        );

        // menu 검증
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND_MENU, null)
        );
        return new MenuResponseDto(
                menu.getId(),
                store.getId(),
                menu.getMenuName(),
                menu.priceToString()
        );
    }

    // 메뉴 수정
    @Transactional
    public MenuResponseDto updateMenu(AuthUser authUser, Long menuId, Long storeId, MenuUpdateRequestDto dto) {
        // OWNER 검증
        if(!authUser.getUserRole().equals(UserRole.OWNER)){
            throw new BaseException(ErrorCode.INVALID_USER_ROLE, null);
        }

        // Store 검증
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new RuntimeException("가게가 없습니다.")
        );

        // menu 검증
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND_MENU, null)
        );

        if (dto.getMenuName() != null) {
            menu.updateMenuName(dto.getMenuName());
        }

        if (dto.getPrice() != null){
            menu.updatePrice(dto.getPrice());
        }

        log.info("메뉴 수정 성공");
        return new MenuResponseDto(
                menu.getId(),
                store.getId(),
                menu.getMenuName(),
                menu.priceToString()
        );
    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(AuthUser authUser, Long menuId, Long storeId) {
        // OWNER 검증
        if(!authUser.getUserRole().equals(UserRole.OWNER)){
            throw new BaseException(ErrorCode.INVALID_USER_ROLE, null);
        }

        // Store 검증
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new RuntimeException("가게가 없습니다.")
        );

        // menu 검증
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND_MENU, null)
        );
        log.info("메뉴 삭제 성공");
        menu.updateDeleteFlag();
    }
}
