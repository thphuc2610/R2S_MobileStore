package com.example.mobile_store.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mobile_store.constant.ApiUrlConstant;
import com.example.mobile_store.dto.OrderDTO;
import com.example.mobile_store.service.OrderService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping(ApiUrlConstant.ORDER)
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(ApiUrlConstant.READ)
    public ResponseEntity<?> getAllOrders(Principal principal) {
        try {
            return ResponseEntity.ok(orderService.findAll(principal));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ApiUrlConstant.READ_ID)
    public ResponseEntity<?> getById(@PathVariable long id, Principal principal) {
        try {
            return ResponseEntity.ok(orderService.findById(id, principal));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping(ApiUrlConstant.CREATE)
    public ResponseEntity<?> create(@RequestBody OrderDTO orderDTO, Principal principal) {
        try {
            return ResponseEntity.ok(orderService.create(orderDTO, principal));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(ApiUrlConstant.UPDATE_ID)
    public ResponseEntity<?> update(@PathVariable(value = "id") long id, OrderDTO orderDTO, Principal principal) {
        try {
            return ResponseEntity.ok(orderService.update(id, orderDTO, principal));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    

    @DeleteMapping(ApiUrlConstant.DELETE_ID)
    public ResponseEntity<Void> delete(@PathVariable(value = "id") long id) {
        orderService.delete(id);
        
        return ResponseEntity.noContent().build();
    }
}
