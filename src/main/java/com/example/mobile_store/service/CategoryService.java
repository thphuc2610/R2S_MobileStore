package com.example.mobile_store.service;

import java.util.List;

import com.example.mobile_store.dto.CategoryDTO;

public interface CategoryService {
    CategoryDTO create(CategoryDTO categoryDTO);

    List<CategoryDTO> findAll();

    CategoryDTO findById(long id);

    CategoryDTO update(long id, CategoryDTO categoryDTO);

    void delete(long id);
}
