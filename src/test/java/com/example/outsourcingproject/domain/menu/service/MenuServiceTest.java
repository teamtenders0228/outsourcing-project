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
import com.example.outsourcingproject.domain.user.enums.UserRole;
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
import java.util.Objects;
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
    public void validateOwner_AuthUser의_id와_Store의_id가_일치하지_않는_경우_에러가_발생한다() {
        // given
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);

        User user = new User("유저1", authUser.getEmail(), "Asdf1234!", "010-1111-1111", "부산시연제구", authUser.getUserRole());
        ReflectionTestUtils.setField(user, "id", 2L);

        Store store = new Store(1L, "홍콩반점", "부산시연제구", "010-1111-1111", StoreCategory.CHINESE, 20000, LocalTime.of(9, 0), LocalTime.of(21, 0), 0.0, false, LocalDateTime.of(2025, 3, 5, 15, 30), user);

        // when & then
        BaseException exception = assertThrows(BaseException.class,
                () -> {
                    if (!Objects.equals(authUser.getId(), store.getUser().getId())) {
                        throw new BaseException(ErrorCode.USERID_NOT_MATCH, null);
                    }
                }
        );
        assertEquals("유저 id가 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    public void menu를_정상적으로_등록한다() {
        // given
        Long menuId = 1L;
        MenuSaveRequestDto request = new MenuSaveRequestDto("피자", 20000);

        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);

        User user = new User("유저1", authUser.getEmail(), "Asdf1234!", "010-1111-1111", "부산시연제구", authUser.getUserRole());
        ReflectionTestUtils.setField(user, "id", authUser.getId());

        Store store = new Store(1L, "홍콩반점", "부산시연제구", "010-1111-1111", StoreCategory.CHINESE, 20000, LocalTime.of(9, 0), LocalTime.of(21, 0), 0.0, false, LocalDateTime.of(2025, 3, 5, 15, 30), user);
        Menu menu = new Menu(request.getMenuName(), request.getPrice(), store);
        ReflectionTestUtils.setField(menu, "id", menuId);

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        //given(Objects.equals(authUser.getId(), store.getUser().getId())).willReturn(true);
        given(menuRepository.save(any(Menu.class))).willReturn(menu);

        // when
        MenuResponseDto menuResponseDto = menuService.saveMenu(authUser, request, store.getId());

        // then
        assertNotNull(menuResponseDto);
        assertEquals("피자", menuResponseDto.getMenuName());
    }

    @Test
    void menu_등록중_store를_찾지_못해_에러가_발생한다() {
        // given
        Long storeId = 1L;
        MenuSaveRequestDto request = new MenuSaveRequestDto("피자", 20000);
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);

        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        BaseException exception = assertThrows(BaseException.class,
                () -> {
                    menuService.saveMenu(authUser, request, storeId);
                });

        // then
        assertEquals("가게 정보를 찾을 수 없습니다.", exception.getMessage());
        assertEquals(ErrorCode.STORE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void menu_전체_조회에_성공한다() {
        // given
        int page = 1;
        int size = 10;
        Long storeId = 1L;
        Pageable pageable = PageRequest.of(page - 1, size);

        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);

        User user = new User("유저1", authUser.getEmail(), "Asdf1234!", "010-1111-1111", "부산시연제구", authUser.getUserRole());
        Store store = new Store(storeId, "홍콩반점", "부산시연제구", "010-1111-1111", StoreCategory.CHINESE, 20000, LocalTime.of(9, 0), LocalTime.of(21, 0), 0.0, false, LocalDateTime.of(2025, 3, 5, 15, 30), user);
        ReflectionTestUtils.setField(store, "id", storeId);

        List<Menu> menuList = List.of(
                new Menu("메뉴1", 10000, store),
                new Menu("메뉴2", 12000, store)
        );
        Page<Menu> mockPage = new PageImpl<>(menuList, pageable, menuList.size());

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(menuRepository.findAllByStoreId(store.getId(), pageable)).willReturn(mockPage);

        // when
        Page<MenuResponseDto> menusPage = menuService.findAllMenu(authUser, page, size, store.getId());

        // then
        assertThat(menusPage.getContent()).hasSize(2);
        assertThat(menusPage.getTotalElements()).isEqualTo(2);
        assertThat(menusPage.getContent().get(0).getMenuName()).isEqualTo("메뉴1");
        assertThat(menusPage.getContent().get(1).getMenuName()).isEqualTo("메뉴2");

    }

    @Test
    void menu_단건_목록_조회에_성공한다() {
        // given
        Long storeId = 1L;
        Long menuId = 1L;
        MenuSaveRequestDto request = new MenuSaveRequestDto("피자", 20000);

        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);

        User user = new User("유저1", authUser.getEmail(), "Asdf1234!", "010-1111-1111", "부산시연제구", authUser.getUserRole());
        Store store = new Store(storeId, "홍콩반점", "부산시연제구", "010-1111-1111", StoreCategory.CHINESE, 20000, LocalTime.of(9, 0), LocalTime.of(21, 0), 0.0, false, LocalDateTime.of(2025, 3, 5, 15, 30), user);
        ReflectionTestUtils.setField(store, "id", storeId);

        Menu mockMenu = new Menu(request.getMenuName(), request.getPrice(), store);
        ReflectionTestUtils.setField(mockMenu, "id", menuId);

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(mockMenu));

        // when
        MenuResponseDto menuResponseDto = menuService.findById(authUser, menuId, storeId);

        // then
        assertEquals(mockMenu.getId(), menuResponseDto.getId());
        assertEquals(mockMenu.getStore().getId(), menuResponseDto.getStoreId());
        assertEquals(mockMenu.getMenuName(), menuResponseDto.getMenuName());
        assertEquals(mockMenu.priceToString(), menuResponseDto.getPrice());
    }

    @Test
    void updateMenu에서_메뉴이름과_가격의_수정에_성공한다() {
        // given
        Long storeId = 1L;
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);

        User user = new User("유저1", authUser.getEmail(), "Asdf1234!", "010-1111-1111", "부산시연제구", authUser.getUserRole());
        ReflectionTestUtils.setField(user, "id", authUser.getId());

        Store store = new Store(storeId, "홍콩반점", "부산시연제구", "010-1111-1111", StoreCategory.CHINESE, 20000, LocalTime.of(9, 0), LocalTime.of(21, 0), 0.0, false, LocalDateTime.of(2025, 3, 5, 15, 30), user);
        ReflectionTestUtils.setField(store, "id", storeId);

        Menu menu = new Menu("메뉴", 5000, store);
        ReflectionTestUtils.setField(menu, "id", menuId);

        MenuUpdateRequestDto request = new MenuUpdateRequestDto("메뉴수정", 10000);

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

        // when
        MenuResponseDto menuResponseDto = menuService.updateMenu(authUser, menu.getId(), store.getId(), request);

        // then
        assertEquals(request.getMenuName(), menuResponseDto.getMenuName());
        assertEquals(String.format("%,d", request.getPrice()), menuResponseDto.getPrice());

    }

    @Test
    void menu를_삭제할_수_있다() {
        // given
        Long menuId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);

        User user = new User("유저1", authUser.getEmail(), "Asdf1234!", "010-1111-1111", "부산시연제구", authUser.getUserRole());
        ReflectionTestUtils.setField(user, "id", authUser.getId());

        Store store = new Store(1L, "홍콩반점", "부산시연제구", "010-1111-1111", StoreCategory.CHINESE, 20000, LocalTime.of(9, 0), LocalTime.of(21, 0), 0.0, false, LocalDateTime.of(2025, 3, 5, 15, 30), user);

        Menu menu = new Menu("메뉴", 5000, store);
        ReflectionTestUtils.setField(menu, "id", menuId);

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));
        menu.updateDeleteFlag();

        // when
        menuService.deleteMenu(authUser, menu.getId(), store.getId());

        // then
        assertTrue(menu.isDeleteFlag());
    }
}

