package com.example.mobile_store.service;

import java.util.List;

import com.example.mobile_store.dto.ImageDTO;

public interface ImageService {
    List<ImageDTO> findAll();

    ImageDTO findById(long id);
}
