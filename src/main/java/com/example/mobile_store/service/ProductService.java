package com.example.mobile_store.service;

import java.util.List;

import com.example.mobile_store.dto.ProductDTO;

public interface ProductService {
    List<ProductDTO> findAll();

    ProductDTO findById(long id);

    ProductDTO create(ProductDTO productDTO);

    ProductDTO update(long id, ProductDTO productDTO);

    void delete(long id);
}
