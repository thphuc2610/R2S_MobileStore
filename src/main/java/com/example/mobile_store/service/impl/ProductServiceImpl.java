package com.example.mobile_store.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mobile_store.dto.ImageDTO;
import com.example.mobile_store.dto.ProductDTO;
import com.example.mobile_store.entity.Category;
import com.example.mobile_store.entity.Image;
import com.example.mobile_store.entity.Product;
import com.example.mobile_store.exception.CategoryNotFoundException;
import com.example.mobile_store.exception.NotFoundException;
import com.example.mobile_store.mapper.ProductMapper;
import com.example.mobile_store.repository.CategoryRepository;
import com.example.mobile_store.repository.ImageRepository;
import com.example.mobile_store.repository.ProductRepository;
import com.example.mobile_store.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductMapper productMapper, ProductRepository productRepository,
            ImageRepository imageRepository, CategoryRepository categoryRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO findById(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        return productMapper.toDTO(product);
    }

    public ProductDTO create(ProductDTO productDTO) {
        String categoryName = productDTO.getCategoryName() != null ? productDTO.getCategoryName() : "Unknown";
        Category category = categoryRepository.findByName(categoryName);
        
        if (category == null) {
            throw new CategoryNotFoundException("Category not found: " + categoryName);
        }
    
        Product product = productMapper.toEntity(productDTO);
        product.setCategory(category);
        product = productRepository.save(product);
    
        List<Image> listImages = new ArrayList<>();
        for (Image image : product.getImages()) {
            image.setProduct(product);
            listImages.add(imageRepository.save(image));
        }
        product.setImages(listImages);
    
        return productMapper.toDTO(product);
    }
    

    public ProductDTO update(long id, ProductDTO productDTO) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        Category category = categoryRepository.findByName(productDTO.getCategoryName());
        if (category == null) {
            throw new CategoryNotFoundException("Category not found: " + productDTO.getCategoryName());
        }
        product = productMapper.toEntity(productDTO);
        product.setId(id);
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setDescription(productDTO.getDescription());
        product.setManufacturer(productDTO.getManufacturer());
        product.setCategory(category);
        product.setCondition(product.getCondition());

        List<ImageDTO> imageDTOs = productDTO.getImageDTOs();
        List<Image> updateImages = new ArrayList<>();

        for (ImageDTO imageDTO : imageDTOs) {
            Long imageId = imageDTO.getId();
            Image image;

            if (imageId != null && imageRepository.existsById(imageId)) {
                // Nếu hình ảnh đã tồn tại trong cơ sở dữ liệu, cập nhật thông tin của nó
                image = imageRepository.findById(imageId).get();
                image.setName(imageDTO.getName());
                image.setUrl(imageDTO.getUrl());
            } else {
                // Nếu hình ảnh chưa tồn tại trong cơ sở dữ liệu, thêm nó vào cơ sở dữ liệu
                image = new Image();
                image.setName(imageDTO.getName());
                image.setUrl(imageDTO.getUrl());
                image.setProduct(product);
            }
            updateImages.add(image);
        }
        product.setImages(updateImages);

        return productMapper.toDTO(productRepository.save(product));
    }

    public void delete(long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException(id);
        }

        // Tìm tất cả các hình ảnh có id sản phẩm tương ứng
        List<Image> images = imageRepository.findByProductId(id);

        // Xóa tất cả các hình ảnh
        if (images != null && !images.isEmpty()) {
            imageRepository.deleteAll(images);
        }

        productRepository.deleteById(id);
    }
}
