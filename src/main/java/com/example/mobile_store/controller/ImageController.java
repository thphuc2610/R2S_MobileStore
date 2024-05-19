package com.example.mobile_store.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mobile_store.constant.ApiUrlConstant;
import com.example.mobile_store.service.ImageService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(ApiUrlConstant.IMAGE)

public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(ApiUrlConstant.READ)
    public ResponseEntity<?> getAllImages() {
        try {
            return ResponseEntity.ok(imageService.findAll());

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ApiUrlConstant.READ_ID)
    public ResponseEntity<?> getById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(imageService.findById(id));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
