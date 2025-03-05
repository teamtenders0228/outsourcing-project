package com.example.outsourcingproject.domain.store.repository;

import com.example.outsourcingproject.domain.store.entity.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByStoreName(String storeName);

    @Query("SELECT COUNT(s) FROM Store s WHERE s.user.id = :userId AND s.deleteAt IS NULL")
    int countByUserId(@Param("userId") Long userId);

    @Query("SELECT s FROM Store s WHERE s.closedFlag = true AND s.deleteAt IS NULL")
    @EntityGraph(attributePaths = "user")
    List<Store> findAllOpenStores();


    @Query("SELECT s FROM Store s WHERE s.id = :storeId AND s.deleteAt IS NULL")
    @EntityGraph(attributePaths = "user")
    Optional<Store> getStoreById(Long storeId);
}
