package com.example.mobile_store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.mobile_store.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT o FROM OrderDetail o WHERE o.id = ?1")
    List<OrderDetail> findByOrderDetailId(Long orderDetailId);

    List<OrderDetail> findByOrderUserId(Long userId);
}
