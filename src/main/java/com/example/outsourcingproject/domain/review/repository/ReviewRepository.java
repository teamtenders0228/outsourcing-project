package com.example.outsourcingproject.domain.review.repository;

import com.example.outsourcingproject.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN r.order o WHERE o.store.id = :storeId")
    List<Review> findByStoreId(Long storeId);

    @Query("SELECT r FROM Review r JOIN r.order o WHERE o.store.id = :storeId ORDER BY r.createdAt DESC")
    Page<Review> findByStoreIdWithPagination(@Param("storeId") Long storeId, Pageable pageable);

    @Query("SELECT r FROM Review r JOIN r.order o WHERE o.user.id = :userId ORDER BY r.createdAt DESC")
    Page<Review> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.order.id = :orderId AND r.order.user.id = :userId")
    boolean existsByOrderIdAndUserId(Long orderId, Long userId);
}
