package com.example.mobile_store;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.mobile_store.dto.ImageDTO;
import com.example.mobile_store.dto.ProductDTO;
import com.example.mobile_store.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final String endpoint = "/api/product";
    private final String id = "/{id}";

    @Test
    public void getAllProducts() throws Exception {
        // given
        List<ProductDTO> products = new ArrayList<>();

        // Tạo ra một sản phẩm đầu tiên
        ProductDTO product1 = new ProductDTO();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(1000.0);
        product1.setQuantity(10L);

        List<ImageDTO> images1 = new ArrayList<>();
        ImageDTO image1_1 = new ImageDTO();
        image1_1.setId(1L);
        image1_1.setName("Image 1_1");
        image1_1.setUrl("http://localhost:8080/image1_1.jpg");
        images1.add(image1_1);

        product1.setImageDTOs(images1);
        products.add(product1);

        // Tạo ra một sản phẩm thứ hai
        ProductDTO product2 = new ProductDTO();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(2000.0);
        product2.setQuantity(20L);

        List<ImageDTO> images2 = new ArrayList<>();
        ImageDTO image2_1 = new ImageDTO();
        image2_1.setId(2L);
        image2_1.setName("Image 2_1");
        image2_1.setUrl("http://localhost:8080/image2_1.jpg");
        images2.add(image2_1);

        ImageDTO image2_2 = new ImageDTO();
        image2_2.setId(3L);
        image2_2.setName("Image 2_2");
        image2_2.setUrl("http://localhost:8080/image2_2.jpg");
        images2.add(image2_2);

        product2.setImageDTOs(images2);
        products.add(product2);

        given(productService.findAll()).willReturn(products);

        // when
        ResultActions response = mockMvc.perform(get(endpoint));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(products.size())))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is("Product 1")))
                .andExpect(jsonPath("$.[0].price", is(1000.0)))
                .andExpect(jsonPath("$.[0].quantity", is(10)))
                .andExpect(jsonPath("$.[0].imageDTOs.size()", is(1)))
                .andExpect(jsonPath("$.[0].imageDTOs.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].imageDTOs.[0].name", is("Image 1_1")))
                .andExpect(jsonPath("$.[0].imageDTOs.[0].url", is("http://localhost:8080/image1_1.jpg")))
                .andExpect(jsonPath("$.[1].id", is(2)))
                .andExpect(jsonPath("$.[1].name", is("Product 2")))
                .andExpect(jsonPath("$.[1].price", is(2000.0)))
                .andExpect(jsonPath("$.[1].quantity", is(20)))
                .andExpect(jsonPath("$.[1].imageDTOs.size()", is(2)))
                .andExpect(jsonPath("$.[1].imageDTOs.[0].id", is(2)))
                .andExpect(jsonPath("$.[1].imageDTOs.[0].name", is("Image 2_1")))
                .andExpect(jsonPath("$.[1].imageDTOs.[0].url", is("http://localhost:8080/image2_1.jpg")))
                .andExpect(jsonPath("$.[1].imageDTOs.[1].id", is(3)))
                .andExpect(jsonPath("$.[1].imageDTOs.[1].name", is("Image 2_2")))
                .andExpect(jsonPath("$.[1].imageDTOs.[1].url", is("http://localhost:8080/image2_2.jpg")));
    }

    @Test
    public void getProductById() throws Exception {
        // given
        long productId = 1L;
        ProductDTO product = new ProductDTO();
        product.setId(productId);
        product.setName("Product 1");
        product.setPrice(1000.0);
        product.setQuantity(10L);

        List<ImageDTO> images = new ArrayList<>();
        ImageDTO image1 = new ImageDTO();
        image1.setId(1L);
        image1.setName("Image 1");
        image1.setUrl("http://localhost:8080/image1.jpg");
        images.add(image1);

        product.setImageDTOs(images);

        given(productService.findById(productId)).willReturn(product);

        // when
        ResultActions response = mockMvc.perform(get(endpoint + id, productId));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Product 1")))
                .andExpect(jsonPath("$.price", is(1000.0)))
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.imageDTOs.size()", is(1)))
                .andExpect(jsonPath("$.imageDTOs.[0].id", is(1)))
                .andExpect(jsonPath("$.imageDTOs.[0].name", is("Image 1")))
                .andExpect(jsonPath("$.imageDTOs.[0].url", is("http://localhost:8080/image1.jpg")));

    }

    @Test
    public void createProduct() throws Exception {
        // given
        ProductDTO newProduct = new ProductDTO();
        newProduct.setName("New Product");
        newProduct.setPrice(1500.0);
        newProduct.setQuantity(20L);

        List<ImageDTO> images = new ArrayList<>();
        ImageDTO image1 = new ImageDTO();
        image1.setId(1L);
        image1.setName("Image 1");
        image1.setUrl("http://localhost:8080/image1.jpg");
        images.add(image1);

        newProduct.setImageDTOs(images);

        given(productService.create(any(ProductDTO.class))).willReturn(newProduct);

        // when
        ResultActions response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newProduct)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is("New Product")))
                .andExpect(jsonPath("$.price", is(1500.0)))
                .andExpect(jsonPath("$.quantity", is(20)))
                .andExpect(jsonPath("$.imageDTOs.size()", is(1)))
                .andExpect(jsonPath("$.imageDTOs.[0].id", is(1)))
                .andExpect(jsonPath("$.imageDTOs.[0].name", is("Image 1")))
                .andExpect(jsonPath("$.imageDTOs.[0].url", is("http://localhost:8080/image1.jpg")));
    }

    @Test
    public void updateProduct() throws Exception {
        // given
        long productId = 1L;
        ProductDTO updatedProduct = new ProductDTO();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(2000.0);
        updatedProduct.setQuantity(30L);

        List<ImageDTO> images = new ArrayList<>();
        ImageDTO image1 = new ImageDTO();
        image1.setId(1L);
        image1.setName("Image 1");
        image1.setUrl("http://localhost:8080/image1.jpg");
        images.add(image1);

        updatedProduct.setImageDTOs(images);

        given(productService.update(anyLong(), any(ProductDTO.class))).willReturn(updatedProduct);

        // when
        ResultActions response = mockMvc.perform(put(endpoint + id, productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedProduct)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Product")))
                .andExpect(jsonPath("$.price", is(2000.0)))
                .andExpect(jsonPath("$.quantity", is(30)))
                .andExpect(jsonPath("$.imageDTOs.size()", is(1)))
                .andExpect(jsonPath("$.imageDTOs.[0].id", is(1)))
                .andExpect(jsonPath("$.imageDTOs.[0].name", is("Image 1")))
                .andExpect(jsonPath("$.imageDTOs.[0].url", is("http://localhost:8080/image1.jpg")));
    }

    @Test
    public void deleteProduct() throws Exception {
        // given
        long productId = 1L;

        // when
        ResultActions response = mockMvc.perform(delete(endpoint + id, productId));

        // then
        response.andExpect(status().isNoContent());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}