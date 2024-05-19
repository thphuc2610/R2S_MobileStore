package com.example.mobile_store.service;

import java.security.Principal;
import java.util.List;

import com.example.mobile_store.dto.OrderDetailDTO;

public interface OrderDetailService {
    List<OrderDetailDTO> findAll(Principal principal);

    OrderDetailDTO findById(long id, Principal principal);
}
