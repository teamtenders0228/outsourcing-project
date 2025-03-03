package com.example.outsourcingproject.domain.menu.controller;

import com.example.outsourcingproject.domain.menu.dto.requestDto.MenuSaveRequestDto;
import com.example.outsourcingproject.domain.menu.dto.requestDto.MenuUpdateRequestDto;
import com.example.outsourcingproject.domain.menu.dto.responseDto.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner")
public class MenuController {

    private final MenuService menuService;

    // 메뉴 등록
    @PostMapping("/menus")
    public ResponseEntity<MenuResponseDto> saveMenu(@Valid @RequestBody MenuSaveRequestDto dto) {
        log.info("메뉴 생성 성공");
        return ResponseEntity.ok(menuService.saveMenu(dto));
    }

    // 메뉴 전체 조회
    @GetMapping("/menus")
    public ResponseEntity<Page<MenuResponseDto>> getAllMenu(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(menuService.findAllMenu(page, size));
    }

    // 메뉴 단건 조회
    @GetMapping("/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> getOneMenu(@PathVariable Long menuId) {
        return ResponseEntity.ok(menuService.findById(menuId));
    }

    // 메뉴 수정
    @PatchMapping("/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequestDto dto
    ) {
        log.info("메뉴 수정 성공");
        return ResponseEntity.ok(menuService.updateMenu(menuId, dto));
    }

    // 메뉴 삭제
    @DeleteMapping("/menus/{menuId}")
    public void deleteMenu(@PathVariable Long menuId) {
        log.info("메뉴 삭제 성공");
        menuService.deleteMenu(menuId);
    }

}
