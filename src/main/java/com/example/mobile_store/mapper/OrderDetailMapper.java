package com.example.mobile_store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.example.mobile_store.dto.OrderDetailDTO;
import com.example.mobile_store.entity.OrderDetail;

@Mapper(componentModel = "spring")
@Component
public interface OrderDetailMapper {
    @Mapping(target = "id", source = "orderDetail.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "orderId", source = "order.id")
    OrderDetailDTO toDTO(OrderDetail orderDetail);

    @Mapping(target = "order.id", source = "id") 
    @Mapping(target = "product.id", source = "productId")
    // @Mapping(target = "order.id", source = "orderId")
    OrderDetail toEntity(OrderDetailDTO orderDetailDTO);
} 