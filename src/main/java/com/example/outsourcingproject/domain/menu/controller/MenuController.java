package com.example.outsourcingproject.domain.menu.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcingproject.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcingproject.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menus")
public class MenuController {

    private final MenuService menuService;

    // 메뉴 등록
    @PostMapping("/stores/{storeId}")
    public ResponseEntity<String> saveMenu(
            @Auth AuthUser authUser,
            @Valid @RequestBody MenuSaveRequestDto dto,
            @PathVariable Long storeId
    ) {
        menuService.saveMenu(authUser, dto, storeId);
        log.info("메뉴 생성 성공");
        return new ResponseEntity<>("message : 메뉴 등록이 완료되었습니다.", HttpStatus.OK);
    }

    // 메뉴 전체 조회
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<Page<MenuResponseDto>> getAllMenu(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(menuService.findAllMenu(authUser, page, size, storeId));
    }

    // 메뉴 단건 조회
    @GetMapping("/{menuId}/stores/{storeId}")
    public ResponseEntity<MenuResponseDto> getOneMenu(
            @Auth AuthUser authUser,
            @PathVariable Long menuId,
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(menuService.findById(authUser, menuId, storeId));
    }

    // 메뉴 수정
    @PatchMapping("/{menuId}/stores/{storeId}")
    public ResponseEntity<String> updateMenu(
            @Auth AuthUser authUser,
            @PathVariable Long menuId,
            @PathVariable Long storeId,
            @Valid @RequestBody MenuUpdateRequestDto dto
    ) {
        menuService.updateMenu(authUser, menuId, storeId, dto);
        log.info("메뉴 수정 성공");
        return new ResponseEntity<>("message : 메뉴 수정이 완료되었습니다.", HttpStatus.OK);
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuId}/stores/{storeId}")
    public ResponseEntity<String> deleteMenu(
            @Auth AuthUser authUser,
            @PathVariable Long menuId,
            @PathVariable Long storeId
    ) {
        log.info("메뉴 삭제 성공");
        menuService.deleteMenu(authUser, menuId, storeId);
        return new ResponseEntity<>("message : 메뉴 삭제가 완료되었습니다.", HttpStatus.OK);
    }

}
