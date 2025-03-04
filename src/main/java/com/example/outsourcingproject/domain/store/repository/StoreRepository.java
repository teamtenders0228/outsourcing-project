package com.example.outsourcingproject.domain.store.repository;

import com.example.outsourcingproject.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository <Store, Long> {
}
