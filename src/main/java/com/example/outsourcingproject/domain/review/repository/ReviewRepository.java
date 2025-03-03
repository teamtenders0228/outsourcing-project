package com.example.outsourcingproject.domain.review.repository;

import com.example.outsourcingproject.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r ORDER BY r.createdAt")
    Page<Review> findAllByCreatedAt(Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.rate BETWEEN :minRate AND :maxRate" +
            " ORDER BY r.createdAt")
    Page<Review> findAllByRateRange(
            @Param("minRate") int minRate,
            @Param("maxRate") int maxRate,
            Pageable pageable
    );
}
