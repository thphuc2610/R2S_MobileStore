package com.example.mobile_store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.example.mobile_store.dto.OrderDTO;
import com.example.mobile_store.entity.Order;

@Mapper(componentModel = "spring")
@Component
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderDetailDTOs", source = "orderDetails")
    OrderDTO toDTO(Order order);

    @Mapping(target = "user.id",source = "userId")
    @Mapping(target = "orderDetails",source = "orderDetailDTOs")
    Order toEntity(OrderDTO orderDTO);
}
