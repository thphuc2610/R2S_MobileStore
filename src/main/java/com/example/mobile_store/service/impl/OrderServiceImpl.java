package com.example.mobile_store.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mobile_store.dto.OrderDTO;
import com.example.mobile_store.dto.OrderDetailDTO;
import com.example.mobile_store.entity.Order;
import com.example.mobile_store.entity.OrderDetail;
import com.example.mobile_store.entity.Product;
import com.example.mobile_store.entity.User;
import com.example.mobile_store.exception.NotFoundException;
import com.example.mobile_store.exception.UnauthorizedAccessException;
import com.example.mobile_store.exception.UserNotFoundException;
import com.example.mobile_store.mapper.OrderMapper;
import com.example.mobile_store.mapper.ProductMapper;
import com.example.mobile_store.repository.OrderDetailRepository;
import com.example.mobile_store.repository.OrderRepository;
import com.example.mobile_store.repository.UserRepository;
import com.example.mobile_store.service.OrderService;
import com.example.mobile_store.service.ProductService;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;
    private final ProductMapper productMapper;

    public OrderServiceImpl(OrderMapper orderMapper, OrderRepository orderRepository, UserRepository userRepository,
            OrderDetailRepository orderDetailRepository, ProductService productServicer, ProductMapper productMapper) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productServicer;
        this.productMapper = productMapper;
    }

    @Override
    public List<OrderDTO> findAll(Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<Order> orders = orderRepository.findByUserId(user.getId());

        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO findById(long id, Principal principal) {
        String username = principal.getName();

        // Find user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        // Find order by id
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        // Check if the order belongs to the authenticated user
        if (order.getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("You do not have permission to access this resource");
        }

        return orderMapper.toDTO(order);
    }

    public OrderDTO create(OrderDTO orderDTO, Principal principal) {
        String username = principal.getName();

        // Tìm người dùng bằng tên người dùng
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Order order = orderMapper.toEntity(orderDTO);
        order.setUser(user);

        int totalQuantity = 0;
        double grandTotal = 0.0;
        List<OrderDetail> listOrderDetails = new ArrayList<>();

        for (OrderDetailDTO orderDetailDTO : orderDTO.getOrderDetailDTOs()) {
            Product product = productMapper.toEntity(productService.findById(orderDetailDTO.getProductId()));

            // Tạo một đối tượng OrderDetail mới và thiết lập thông tin
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setQuantity(orderDetailDTO.getQuantity());
            orderDetail.setUnitPrice(orderDetailDTO.getUnitPrice());

            // Tính toán giá trị price cho OrderDetail
            double price = orderDetail.getQuantity() * orderDetail.getUnitPrice();
            orderDetail.setPrice(price);

            // Cập nhật tổng số lượng và tổng giá trị của đơn hàng
            totalQuantity += orderDetail.getQuantity();
            grandTotal += price;

            listOrderDetails.add(orderDetail);
        }

        listOrderDetails = orderDetailRepository.saveAll(listOrderDetails);

        for (OrderDetail orderDetail : listOrderDetails) {
            orderDetail.setOrder(order);
        }

        // Cập nhật thông tin tổng số lượng và tổng giá trị của đơn hàng
        order.setTotalQuantity(totalQuantity);
        order.setGrandTotal(grandTotal);
        order.setOrderDetails(listOrderDetails);

        order = orderRepository.save(order);

        return orderMapper.toDTO(order);
    }

    public OrderDTO update(long id, OrderDTO orderDTO, Principal principal) {
        String username = principal.getName();

        // Find user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        // Update the basic information of the order
        Order order = orderMapper.toEntity(orderDTO);
        order.setId(id); // Ensure the ID remains the same
        order.setUser(user);

        int totalQuantity = 0;
        double grandTotal = 0.0;
        List<OrderDetail> updatedOrderDetails = new ArrayList<>();

        // Process each order detail DTO
        for (OrderDetailDTO orderDetailDTO : orderDTO.getOrderDetailDTOs()) {
            Product product = productMapper.toEntity(productService.findById(orderDetailDTO.getProductId()));

            // Create a new OrderDetail or update an existing one
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailDTO.getId())
                    .orElse(new OrderDetail());
            orderDetail.setProduct(product);
            orderDetail.setQuantity(orderDetailDTO.getQuantity());
            orderDetail.setUnitPrice(orderDetailDTO.getUnitPrice());

            // Calculate price for OrderDetail
            double price = orderDetail.getQuantity() * orderDetail.getUnitPrice();
            orderDetail.setPrice(price);

            // Update total quantity and grand total of the order
            totalQuantity += orderDetail.getQuantity();
            grandTotal += price;

            updatedOrderDetails.add(orderDetail);
        }

        updatedOrderDetails = orderDetailRepository.saveAll(updatedOrderDetails);

        for (OrderDetail orderDetail : updatedOrderDetails) {
            orderDetail.setOrder(order);
        }

        // Update the total quantity and grand total of the order
        order.setTotalQuantity(totalQuantity);
        order.setGrandTotal(grandTotal);
        order.setOrderDetails(updatedOrderDetails);

        order = orderRepository.save(order);

        return orderMapper.toDTO(order);
    }

    public void delete(long id) {
        if (!orderRepository.existsById(id)) {
            throw new NotFoundException(id);
        }

        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderDetailId(id);

        if (orderDetails != null && !orderDetails.isEmpty()) {
            orderDetailRepository.deleteAll(orderDetails);
        }

        orderRepository.deleteById(id);
    }
}
