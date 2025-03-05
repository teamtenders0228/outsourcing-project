package com.example.outsourcingproject.domain.menu.service;

import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.menu.dto.requestDto.MenuSaveRequestDto;
import com.example.outsourcingproject.domain.menu.dto.responseDto.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    // @Mock : 테스트 대상 아님
    // @InjectMocks : 테스트 대상
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void menu_등록중_store를_찾지_못해_에러가_발생한다(){
        // given
        Long storeId = 1L;
        MenuSaveRequestDto request = new MenuSaveRequestDto("피자", 20000);
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);

        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        BaseException exception = assertThrows(BaseException.class,
                () -> {menuService.saveMenu(authUser, request, storeId);
        });

        // then
        assertEquals("가게 정보를 찾을 수 없습니다.", exception.getMessage());
        assertEquals(ErrorCode.STORE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void menu를_정상적으로_등록한다(){
        // given
        Long storeId = 1L;
        MenuSaveRequestDto request = new MenuSaveRequestDto("피자", 20000);
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        Store store = new Store("홍콩반점", "부산시연제구", "010-1111-1111", StoreCategory.CHINESE, 20000, LocalTime.of(9,0), LocalTime.of(21,0), 0.0);
        Menu menu = new Menu(request.getMenuName(), request.getPrice(), store);

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(menuRepository.save(any())).willReturn(menu);

        // when
        MenuResponseDto result = menuService.saveMenu(authUser, request, storeId);

        // then
        assertNotNull(result);
    }


}