package com.example.mobile_store.dto;

import java.util.List;

import com.example.mobile_store.entity.Product.ProductCondition;

import lombok.Data;

@Data
public class ProductDTO {
    private long id;

    private String name;

    private double price;

    private long quantity;

    private String description;

    private String manufacturer;

    private String categoryName;

    private ProductCondition condition;

    private List<ImageDTO> imageDTOs;
}
