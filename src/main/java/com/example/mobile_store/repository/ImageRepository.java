package com.example.mobile_store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.mobile_store.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
    @Query("SELECT i FROM Image i WHERE i.product.id = ?1")
    List<Image> findByProductId(Long productId);
}
