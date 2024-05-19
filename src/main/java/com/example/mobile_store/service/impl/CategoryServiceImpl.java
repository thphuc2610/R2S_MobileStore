package com.example.mobile_store.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mobile_store.dto.CategoryDTO;
import com.example.mobile_store.entity.Category;
import com.example.mobile_store.exception.NotFoundException;
import com.example.mobile_store.mapper.CategoryMapper;
import com.example.mobile_store.repository.CategoryRepository;
import com.example.mobile_store.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO findById(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        return categoryMapper.toDTO(category);
    }

    public CategoryDTO create(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);

        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    public CategoryDTO update(long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    
        category.setName(categoryDTO.getName());
    
        return categoryMapper.toDTO(categoryRepository.save(category));
    }
    

    public void delete(long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException(id);
        }

        categoryRepository.deleteById(id);
    }
}
