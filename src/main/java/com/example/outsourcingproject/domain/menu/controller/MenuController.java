package com.example.outsourcingproject.domain.menu.controller;

import com.example.outsourcingproject.common.annotation.Auth;
import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.domain.menu.dto.requestDto.MenuSaveRequestDto;
import com.example.outsourcingproject.domain.menu.dto.requestDto.MenuUpdateRequestDto;
import com.example.outsourcingproject.domain.menu.dto.responseDto.MenuResponseDto;
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
@RequestMapping("/api/v1/stores") // store 기능 구현되면 /{storeId} 넣기
public class MenuController {

    private final MenuService menuService;

    // 메뉴 등록
    @PostMapping("/menus")
    public ResponseEntity<String> saveMenu(
            @Auth AuthUser authUser,
            @Valid @RequestBody MenuSaveRequestDto dto
    ) {
        menuService.saveMenu(authUser,dto);
        log.info("메뉴 생성 성공");
        return new ResponseEntity<>("message : 메뉴 등록이 완료되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<String> updateMenu(
            @Auth AuthUser authUser,
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequestDto dto
    ) {
        menuService.updateMenu(authUser, menuId, dto);
        log.info("메뉴 수정 성공");
        return new ResponseEntity<>("message : 메뉴 수정이 완료되었습니다.", HttpStatus.OK);
    }

    // 메뉴 삭제
    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<String> deleteMenu(
            @Auth AuthUser authUser,
            @PathVariable Long menuId
    ) {
        log.info("메뉴 삭제 성공");
        menuService.deleteMenu(authUser, menuId);
        return new ResponseEntity<>("message : 메뉴 삭제가 완료되었습니다.", HttpStatus.OK);
    }

}
