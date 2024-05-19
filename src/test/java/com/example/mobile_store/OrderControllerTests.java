package com.example.mobile_store;

import static org.mockito.ArgumentMatchers.any;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.mobile_store.dto.OrderDTO;
import com.example.mobile_store.dto.OrderDetailDTO;
import com.example.mobile_store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Ignore spring security
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private final String endpoint = "/api/order";
    private final String id = "/{id}";

    @Test
    public void getAllOrders() throws Exception {
        // given
        List<OrderDTO> orders = new ArrayList<>();
        OrderDTO order1 = new OrderDTO();
        order1.setId(1L);
        order1.setTotalQuantity(2L);
        order1.setGrandTotal(10000.0D);
        order1.setUserId(2L);

        List<OrderDetailDTO> orderDetails = new ArrayList<>();
        OrderDetailDTO orderDetail1 = new OrderDetailDTO();
        orderDetail1.setId(1L);
        orderDetail1.setQuantity(1L);
        orderDetail1.setUnitPrice(5000.0D);
        orderDetail1.setPrice(5000.0D);
        orderDetail1.setOrderId(1L);
        orderDetail1.setProductId(1001L);
        orderDetails.add(orderDetail1);

        order1.setOrderDetailDTOs(orderDetails);

        // Add order1 to the list of orders
        orders.add(order1);

        Principal principal = Mockito.mock(Principal.class);

        Mockito.when(principal.getName()).thenReturn("username");

        given(orderService.findAll(principal)).willReturn(orders);

        // when
        ResultActions response = mockMvc.perform(get(endpoint));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", org.hamcrest.Matchers.is(orders.size())))
                .andExpect(jsonPath("$.[0].id", org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.[0].totalQuantity", org.hamcrest.Matchers.is(2)))
                .andExpect(jsonPath("$.[0].grandTotal", org.hamcrest.Matchers.is(10000.0)))
                .andExpect(jsonPath("$.[0].userId", org.hamcrest.Matchers.is(2)))
                .andExpect(jsonPath("$.[0].orderDetailDTOs.size()", org.hamcrest.Matchers.is(orderDetails.size())))
                .andExpect(jsonPath("$.[0].orderDetailDTOs.[0].id", org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.[0].orderDetailDTOs.[0].quantity", org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.[0].orderDetailDTOs.[0].unitPrice", org.hamcrest.Matchers.is(5000.0)))
                .andExpect(jsonPath("$.[0].orderDetailDTOs.[0].price", org.hamcrest.Matchers.is(5000.0)))
                .andExpect(jsonPath("$.[0].orderDetailDTOs.[0].orderId", org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.[0].orderDetailDTOs.[0].productId", org.hamcrest.Matchers.is(1001)));
    }

    @Test
    public void getOrderById() throws Exception {
        // given
        long orderId = 1L;
        OrderDTO order = new OrderDTO();
        order.setId(orderId);
        order.setTotalQuantity(2L);
        order.setGrandTotal(10000.0D);
        order.setUserId(2L);

        List<OrderDetailDTO> orderDetails = new ArrayList<>();
        OrderDetailDTO orderDetail = new OrderDetailDTO();
        orderDetail.setId(1L);
        orderDetail.setQuantity(1L);
        orderDetail.setUnitPrice(5000.0D);
        orderDetail.setPrice(5000.0D);
        orderDetail.setOrderId(1L);
        orderDetail.setProductId(1001L);
        orderDetails.add(orderDetail);

        order.setOrderDetailDTOs(orderDetails);

        Principal principal = Mockito.mock(Principal.class);

        Mockito.when(principal.getName()).thenReturn("username");

        given(orderService.findById(orderId, principal)).willReturn(order);
        // when
        ResultActions response = mockMvc.perform(get(endpoint + id, orderId));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", org.hamcrest.Matchers.is((int) orderId)))
                .andExpect(jsonPath("$.totalQuantity", org.hamcrest.Matchers.is(2)))
                .andExpect(jsonPath("$.grandTotal", org.hamcrest.Matchers.is(10000.0)))
                .andExpect(jsonPath("$.userId", org.hamcrest.Matchers.is(2)))
                .andExpect(jsonPath("$.orderDetailDTOs.size()", org.hamcrest.Matchers.is(orderDetails.size())))
                .andExpect(jsonPath("$.orderDetailDTOs.[0].id", org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.orderDetailDTOs.[0].quantity", org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.orderDetailDTOs.[0].unitPrice", org.hamcrest.Matchers.is(5000.0)))
                .andExpect(jsonPath("$.orderDetailDTOs.[0].price", org.hamcrest.Matchers.is(5000.0)))
                .andExpect(jsonPath("$.orderDetailDTOs.[0].orderId", org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.orderDetailDTOs.[0].productId", org.hamcrest.Matchers.is(1001)));
    }

    @Test
    public void createOrder() throws Exception {
        // given
        OrderDTO newOrder = new OrderDTO();
        newOrder.setTotalQuantity(2);
        newOrder.setGrandTotal(10000.0);
        newOrder.setUserId(2L);

        List<OrderDetailDTO> orderDetails = new ArrayList<>();
        OrderDetailDTO newOrderDetail = new OrderDetailDTO();
        newOrderDetail.setQuantity(1);
        newOrderDetail.setUnitPrice(5000.0);
        newOrderDetail.setProductId(1001L);
        orderDetails.add(newOrderDetail);

        newOrder.setOrderDetailDTOs(orderDetails);

        given(orderService.create(any(OrderDTO.class), any(Principal.class))).willReturn(newOrder);

        // when
        ResultActions response = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newOrder)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.totalQuantity", org.hamcrest.Matchers.is(2)))
                .andExpect(jsonPath("$.grandTotal", org.hamcrest.Matchers.is(10000.0)))
                .andExpect(jsonPath("$.userId", org.hamcrest.Matchers.is(2)))
                .andExpect(jsonPath("$.orderDetailDTOs", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$.orderDetailDTOs[0].quantity", org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.orderDetailDTOs[0].unitPrice", org.hamcrest.Matchers.is(5000.0)))
                .andExpect(jsonPath("$.orderDetailDTOs[0].price", org.hamcrest.Matchers.is(5000.0)))
                .andExpect(jsonPath("$.orderDetailDTOs[0].productId", org.hamcrest.Matchers.is(1001L)));
    }

    @Test
    public void deleteOrder() throws Exception {
        // given
        long orderId = 1L;

        // when
        ResultActions response = mockMvc.perform(delete(endpoint + id, orderId));

        // then
        response.andExpect(status().isNoContent()).andDo(print());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}