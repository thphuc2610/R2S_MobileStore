package com.example.mobile_store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mobile_store.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByUserId(long userId);
}
