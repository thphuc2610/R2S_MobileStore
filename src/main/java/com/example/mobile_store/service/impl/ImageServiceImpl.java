package com.example.mobile_store.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mobile_store.dto.ImageDTO;
import com.example.mobile_store.entity.Image;
import com.example.mobile_store.exception.NotFoundException;
import com.example.mobile_store.mapper.ImageMapper;
import com.example.mobile_store.repository.ImageRepository;
import com.example.mobile_store.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageMapper imageMapper;
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageMapper imageMapper, ImageRepository imageRepository) {
        this.imageMapper = imageMapper;
        this.imageRepository = imageRepository;
    }

    public List<ImageDTO> findAll() {
        return imageRepository.findAll().stream()
                .map(imageMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ImageDTO findById(long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        return imageMapper.toDTO(image);
    }
}
