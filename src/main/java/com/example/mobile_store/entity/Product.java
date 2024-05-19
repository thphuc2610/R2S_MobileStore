package com.example.mobile_store.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "product")
public class Product {
    public enum ProductCondition {
        NEW_PRODUCT,
        OLD_PRODUCT,
        REFURBISHED_PRODUCT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;

    @Column(name = "product_name", length = 256)
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private long quantity;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "manufacturer", length = 256)
    private String manufacturer;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_name")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition")
    private ProductCondition condition;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Image> images;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderDetail> orderDetails; 
}
