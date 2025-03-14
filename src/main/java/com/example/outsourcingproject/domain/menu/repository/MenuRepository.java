package com.example.outsourcingproject.domain.menu.repository;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findById(Long id);

    default Menu findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new BaseException(ErrorCode.MENU_NOT_FOUND, null));
    }

    List<Menu> findAllByStoreId(Long storeId);

    @Query("SELECT m FROM Menu m WHERE m.store.id = :storeId "+
            "ORDER BY m.updatedAt DESC")
    Page<Menu> findAllByStoreId(
            @Param("storeId") Long storeId,
            Pageable pageable
    );
}
