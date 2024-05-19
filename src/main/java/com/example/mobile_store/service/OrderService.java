package com.example.mobile_store.service;

import java.security.Principal;
import java.util.List;

import com.example.mobile_store.dto.OrderDTO;

public interface OrderService {

    List<OrderDTO> findAll(Principal principal);

    OrderDTO findById(long id, Principal principal);

    OrderDTO create(OrderDTO orderDTO, Principal principal); 
    
    OrderDTO update(long id, OrderDTO orderDTO, Principal principal);

    void delete(long id);
}