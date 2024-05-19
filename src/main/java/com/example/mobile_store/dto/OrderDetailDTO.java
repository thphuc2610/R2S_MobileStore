package com.example.mobile_store.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private long id;

    private long quantity;

    private double unitPrice;

    private double price;

    private long orderId;

    private long productId;
}
