package com.example.outsourcingproject.domain.store.repository;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findById(Long id);

    default Store findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND, null));
    }

    List<Store> findByUser_Id(Long userId);
}
