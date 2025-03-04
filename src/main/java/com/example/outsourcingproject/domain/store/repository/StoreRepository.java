package com.example.outsourcingproject.domain.store.repository;

import com.example.outsourcingproject.domain.store.entity.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByStoreName(String storeName);

    int countByUserId(Long userId);
    @Query("SELECT s FROM Store s WHERE s.closedFlag = true")
    @EntityGraph(attributePaths = "user")
    List<Store> findAllOpenStores();


    @EntityGraph(attributePaths = "user")
    Optional<Store> getStoreById(Long storeId);
}
