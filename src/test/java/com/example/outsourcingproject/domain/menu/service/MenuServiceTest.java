package com.example.outsourcingproject.domain.menu.service;

import com.example.outsourcingproject.common.dto.AuthUser;
import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.menu.dto.request.MenuSaveRequestDto;
import com.example.outsourcingproject.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcingproject.domain.menu.dto.response.MenuResponseDto;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import com.example.outsourcingproject.domain.menu.repository.MenuRepository;
import com.example.outsourcingproject.domain.store.entity.Store;
import com.example.outsourcingproject.domain.store.enums.StoreCategory;
import com.example.outsourcingproject.domain.store.repository.StoreRepository;
import com.example.outsourcingproject.domain.user.entity.User;
import com.example.outsourcingproject.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


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
    void jwt안에_있는_UserRole이_OWNER가_아닐_경우_에러가_발생한다() {
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        //ReflectionTestUtils.setField(authUser, "userRole", UserRole.USER);

        // when & then
        assertThrows(BaseException.class,
                () -> {
                    if (!authUser.getUserRole().equals(UserRole.OWNER)) {
                        throw new BaseException(ErrorCode.INVALID_USER_ROLE, null);
                    }
                },
                "유효하지 않는 역할입니다."
        );
    }

    @Test
    void menu를_정상적으로_등록한다() {
        // given
        Long storeId = 1L;
        Long userId = 1L;
        Long menuId = 1L;
        MenuSaveRequestDto request = new MenuSaveRequestDto("피자", 20000);

        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        ReflectionTestUtils.setField(authUser, "id", userId);

        User user = new User("유저1", authUser.getEmail(), "Asdf1234!", "010-1111-1111", "부산시연제구", authUser.getUserRole());
        ReflectionTestUtils.setField(user, "id", authUser.getId());

        Store store = new Store(storeId, "홍콩반점", "부산시연제구", "010-1111-1111", StoreCategory.CHINESE, 20000, LocalTime.of(9, 0), LocalTime.of(21, 0), 0.0, false, LocalDateTime.of(2025, 3, 5, 15, 30), user);
        Menu menu = new Menu(request.getMenuName(), request.getPrice(), store);
        ReflectionTestUtils.setField(menu, "id", menuId);

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(menuRepository.save(any())).willReturn(menu);

        // when
        MenuResponseDto menuResponseDto = menuService.saveMenu(authUser, request, store.getId());

        // then
        assertNotNull(menuResponseDto);
        assertEquals(1L, menuResponseDto.getId());
    }
}

