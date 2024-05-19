package com.example.mobile_store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.example.mobile_store.dto.ProductDTO;
import com.example.mobile_store.entity.Product;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "images", target = "imageDTOs")
    ProductDTO toDTO(Product product);

    @Mapping(source = "categoryName", target = "category.name")
    @Mapping(source = "imageDTOs", target = "images")
    Product toEntity(ProductDTO productDTO);
}