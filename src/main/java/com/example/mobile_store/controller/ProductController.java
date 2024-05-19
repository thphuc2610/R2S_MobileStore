package com.example.mobile_store.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mobile_store.constant.ApiUrlConstant;
import com.example.mobile_store.dto.ProductDTO;
import com.example.mobile_store.service.ProductService;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(ApiUrlConstant.PRODUCT)

public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    
    @GetMapping(ApiUrlConstant.READ)
    public ResponseEntity<?> getAllProducts() {
        try {
            return ResponseEntity.ok(productService.findAll());

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ApiUrlConstant.READ_ID)
    public ResponseEntity<?> getById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(productService.findById(id));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApiUrlConstant.CREATE)
    public ResponseEntity<?> create(@RequestBody ProductDTO productDTO) {
        try {
            return ResponseEntity.ok(productService.create(productDTO));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(ApiUrlConstant.UPDATE_ID)
    public ResponseEntity<?> update(@PathVariable(value = "id") long id, @RequestBody ProductDTO productDTO) {
        try {
            return ResponseEntity.ok(productService.update(id, productDTO));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(ApiUrlConstant.DELETE_ID)
    public ResponseEntity<Void> delete(@PathVariable(value = "id") long id) {
        productService.delete(id);

        return ResponseEntity.noContent().build();
    }
}