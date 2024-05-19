package com.example.mobile_store.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {
    private long id;

    private long totalQuantity;

    private Double grandTotal;
    
    private long userId;

    private LocalDateTime orderDate;

    private List<OrderDetailDTO> orderDetailDTOs;

    
}