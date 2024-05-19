package com.example.mobile_store;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.mobile_store.dto.OrderDetailDTO;
import com.example.mobile_store.service.OrderDetailService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Ignore spring security
public class OrderDetailControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderDetailService orderDetailService;

    @Test
    public void getAllOrderDetails() throws Exception {
        // given
        List<OrderDetailDTO> orderDetails = new ArrayList<>();
        OrderDetailDTO orderDetail1 = new OrderDetailDTO();
        orderDetail1.setId(1L);
        orderDetail1.setQuantity(2L);
        orderDetail1.setUnitPrice(10000.0D);
        orderDetail1.setPrice(20000.0D);
        orderDetail1.setOrderId(1L);
        orderDetail1.setProductId(1001L);
        orderDetails.add(orderDetail1);

        OrderDetailDTO orderDetail2 = new OrderDetailDTO();
        orderDetail2.setId(2L);
        orderDetail2.setQuantity(3L);
        orderDetail2.setUnitPrice(15000.0D);
        orderDetail2.setPrice(45000.0D);
        orderDetail2.setOrderId(2L);
        orderDetail2.setProductId(1002L);
        orderDetails.add(orderDetail2);

        Principal mockPrincipal = () -> "testUser";
        given(orderDetailService.findAll(mockPrincipal)).willReturn(orderDetails);

        // when
        ResultActions response = mockMvc.perform(get("/order-details")
                .principal(mockPrincipal));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(orderDetails.size())))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].quantity", is(2)))
                .andExpect(jsonPath("$[0].unitPrice", is(10000.0)))
                .andExpect(jsonPath("$[0].price", is(20000.0)))
                .andExpect(jsonPath("$[0].orderId", is(1)))
                .andExpect(jsonPath("$[0].productId", is(1001)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].quantity", is(3)))
                .andExpect(jsonPath("$[1].unitPrice", is(15000.0)))
                .andExpect(jsonPath("$[1].price", is(45000.0)))
                .andExpect(jsonPath("$[1].orderId", is(2)))
                .andExpect(jsonPath("$[1].productId", is(1002)));
    }

    @Test
    public void getOrderDetailById() throws Exception {
        // given
        long orderDetailId = 1L;
        OrderDetailDTO orderDetail = new OrderDetailDTO();
        orderDetail.setId(orderDetailId);
        orderDetail.setQuantity(2L);
        orderDetail.setUnitPrice(10000.0D);
        orderDetail.setPrice(20000.0D);
        orderDetail.setOrderId(1L);
        orderDetail.setProductId(1001L);

        Principal mockPrincipal = () -> "testUser";
        given(orderDetailService.findById(orderDetailId, mockPrincipal)).willReturn(orderDetail);

        // when
        ResultActions response = mockMvc.perform(get("/order-details/{id}", orderDetailId)
                .principal(mockPrincipal));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is((int) orderDetailId)))
                .andExpect(jsonPath("$.quantity", is(2)))
                .andExpect(jsonPath("$.unitPrice", is(10000.0)))
                .andExpect(jsonPath("$.price", is(20000.0)))
                .andExpect(jsonPath("$.orderId", is(1)))
                .andExpect(jsonPath("$.productId", is(1001)));
    }
}
