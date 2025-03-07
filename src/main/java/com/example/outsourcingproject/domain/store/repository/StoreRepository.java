package com.example.outsourcingproject.domain.store.repository;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findById(Long id);

    default Store findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND, null));
    }

    List<Store> findByUser_Id(Long userId); // 삭제 혹은 영업 종료한 가게 인지 삭제된 유저인지

    boolean existsByStoreName(String storeName);

    @Query("SELECT COUNT(s) FROM Store s WHERE s.user.id = :userId AND s.deleteAt IS NULL")
    int countByUserId(@Param("userId") Long userId);

    // 오너 유저용 매장 조회
    @Query("SELECT s FROM Store s WHERE s.id = :storeId AND s.user.id = :userId AND s.deleteAt IS NULL")
    Optional<Store> findByIdAndUserId(@Param("storeId") Long storeId, @Param("userId") Long userId);

    @Query("SELECT s FROM Store s WHERE s.user.id= :userId AND s.deleteAt IS NULL")
    List<Store> findAllByUserId(@Param("userId") Long userId);

    @EntityGraph(attributePaths = "user")
    Optional<Store> getStoreById(Long storeId);

    // 일반 유저용 매장 조회
    @Query("SELECT s FROM Store s WHERE s.closedFlag = true AND s.deleteAt IS NULL")
    @EntityGraph(attributePaths = "user")
    List<Store> findAllOpenStores();

    @Query("SELECT s FROM Store s WHERE s.id = :storeId AND s.closedFlag = true AND s.deleteAt IS NULL")
    @EntityGraph(attributePaths = "user")
    Optional<Store> getOpenStoreById(Long storeId);
}
