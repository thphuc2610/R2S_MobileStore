package com.example.mobile_store.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mobile_store.constant.ApiUrlConstant;
import com.example.mobile_store.service.OrderDetailService;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(ApiUrlConstant.ORDER_DETAIL)

public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping(ApiUrlConstant.READ)
    public ResponseEntity<?> getAllOrderDetails(Principal principal) {
        try {
            return ResponseEntity.ok(orderDetailService.findAll(principal));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ApiUrlConstant.READ_ID)
    public ResponseEntity<?> getById(@PathVariable long id, Principal principal) {
        try {
            return ResponseEntity.ok(orderDetailService.findById(id, principal));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
