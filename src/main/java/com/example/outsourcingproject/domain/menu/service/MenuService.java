package com.example.outsourcingproject.domain.menu.service;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.menu.dto.requestDto.MenuSaveRequestDto;
import com.example.outsourcingproject.domain.menu.dto.requestDto.MenuUpdateRequestDto;
import com.example.outsourcingproject.domain.menu.dto.responseDto.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    // 메뉴 등록
    @Transactional
    public MenuResponseDto saveMenu(MenuSaveRequestDto dto) {
        Menu menu = new Menu(dto.getMenuName(), dto.getPrice());
        menuRepository.save(menu);

        return new MenuResponseDto(
                menu.getId(),
                menu.getMenuName(),
                menu.priceToString()
        );
    }

    // 메뉴 전체 조회
    @Transactional(readOnly = true)
    public Page<MenuResponseDto> findAllMenu(int page, int size) {
        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size, Sort.by("updatedAt").descending());
        Page<Menu> menuPage = menuRepository.findAll(pageable);

        List<MenuResponseDto> dtoList = menuPage.getContent().stream()
                .map(MenuResponseDto::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, menuPage.getTotalElements());
    }

    // 메뉴 단건 조회
    @Transactional(readOnly = true)
    public MenuResponseDto findById(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND_MENU, "메뉴")
        );
        return new MenuResponseDto(
                menu.getId(),
                menu.getMenuName(),
                menu.priceToString()
        );
    }

    // 메뉴 수정
    @Transactional
    public MenuResponseDto updateMenu(Long menuId, MenuUpdateRequestDto dto) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND_MENU, "메뉴")
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
                menu.getMenuName(),
                menu.priceToString()
        );
    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND_MENU, "메뉴")
        );
        log.info("메뉴 삭제 성공");
        menu.updateDeleteFlag();
    }
}
