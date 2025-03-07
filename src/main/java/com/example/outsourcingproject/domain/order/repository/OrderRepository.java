package com.example.outsourcingproject.domain.order.repository;

import com.example.outsourcingproject.common.exception.BaseException;
import com.example.outsourcingproject.common.exception.ErrorCode;
import com.example.outsourcingproject.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserId(Long id);

    default Order findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new BaseException(ErrorCode.ORDER_NOT_FOUND, null));
    }
}
