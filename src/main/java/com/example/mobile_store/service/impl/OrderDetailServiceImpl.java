package com.example.mobile_store.service.impl;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mobile_store.dto.OrderDetailDTO;
import com.example.mobile_store.entity.OrderDetail;
import com.example.mobile_store.entity.User;
import com.example.mobile_store.exception.NotFoundException;
import com.example.mobile_store.exception.UnauthorizedAccessException;
import com.example.mobile_store.exception.UserNotFoundException;
import com.example.mobile_store.mapper.OrderDetailMapper;
import com.example.mobile_store.repository.OrderDetailRepository;
import com.example.mobile_store.repository.UserRepository;
import com.example.mobile_store.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailMapper orderDetailMapper;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;

    public OrderDetailServiceImpl(OrderDetailMapper orderDetailMapper, OrderDetailRepository orderDetailRepository,
            UserRepository userRepository) {
        this.orderDetailMapper = orderDetailMapper;
        this.orderDetailRepository = orderDetailRepository;
        this.userRepository = userRepository;
    }

    public List<OrderDetailDTO> findAll(Principal principal) {
        String username = principal.getName();

        // Find user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        // Find all order details associated with the user
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderUserId(user.getId());

        // Map order details to DTOs and return
        return orderDetails.stream()
                .map(orderDetailMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OrderDetailDTO findById(long id, Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        if (orderDetail.getOrder().getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("You do not have permission to access this resource");
        }

        return orderDetailMapper.toDTO(orderDetail);
    }
}
