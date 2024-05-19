package com.example.mobile_store.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mobile_store.dto.CategoryDTO;
import com.example.mobile_store.service.CategoryService;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.mobile_store.constant.ApiUrlConstant;

@RestController
@RequestMapping(ApiUrlConstant.CATEGORY)
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(ApiUrlConstant.READ)
    public ResponseEntity<?> getAllCategories() {
        try {
            return ResponseEntity.ok(categoryService.findAll());

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ApiUrlConstant.READ_ID)
    public ResponseEntity<?> getById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(categoryService.findById(id));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApiUrlConstant.CREATE)
    public ResponseEntity<?> create(@RequestBody CategoryDTO categoryDTO) {
        try {
            return ResponseEntity.ok(categoryService.create(categoryDTO));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(ApiUrlConstant.UPDATE_ID)
    public ResponseEntity<?> update(@PathVariable(value = "id") long id, @RequestBody CategoryDTO categoryDTO) {
        try {
            return ResponseEntity.ok(categoryService.update(id, categoryDTO));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(ApiUrlConstant.DELETE_ID)
    public ResponseEntity<Void> delete(@PathVariable(value = "id") long id) {
        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
