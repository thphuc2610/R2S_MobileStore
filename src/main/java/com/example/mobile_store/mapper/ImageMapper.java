package com.example.mobile_store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.example.mobile_store.dto.ImageDTO;
import com.example.mobile_store.entity.Image;

@Mapper(componentModel = "spring")
@Component
public interface ImageMapper {
    @Mapping(target = "id", source = "image.id")
    ImageDTO toDTO(Image image);

    @Mapping(target = "product.id", source = "id")
    Image toEntity(ImageDTO imageDTO);
}
