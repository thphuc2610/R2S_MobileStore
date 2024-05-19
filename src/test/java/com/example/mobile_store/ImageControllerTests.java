package com.example.mobile_store;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.mobile_store.dto.ImageDTO;
import com.example.mobile_store.service.ImageService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Ignore spring security
public class ImageControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    private final String endpoint = "/api/image";
    private final String id = "/{id}";

    @Test
    public void getAllImages() throws Exception {
        // given
        List<ImageDTO> images = new ArrayList<>();
        ImageDTO image1 = new ImageDTO();
        image1.setId(1L);
        image1.setName("image1.jpg");
        image1.setUrl("http://localhost:8080/api/upload/files/image1.jpg");
                
        ImageDTO image2 = new ImageDTO();
        image2.setId(2L);
        image2.setName("image2.jpg");
        image2.setUrl("http://localhost:8080/api/upload/files/image2.jpg");
        given(imageService.findAll()).willReturn(images);

        // when
        ResultActions response = mockMvc.perform(get(endpoint));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", org.hamcrest.Matchers.is(images.size())))
                .andExpect(jsonPath("$.[0].id", org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.[0].name").value(image1.getName()))
                .andExpect(jsonPath("$.[0].url").value(image1.getUrl()))
                .andExpect(jsonPath("$.[1].id", org.hamcrest.Matchers.is(2)))
                .andExpect(jsonPath("$.[1].name").value(image2.getName()))
                .andExpect(jsonPath("$.[1].url").value(image2.getUrl()));
    }

    @Test
    public void getById() throws Exception {
        // given
        long imageId = 1L;
        ImageDTO image = new ImageDTO();
        image.setId(imageId);
        image.setName("image1");
        image.setUrl("http://localhost:8080/api/upload/files/image1.jpg");
        given(imageService.findById(imageId)).willReturn(image);

        // when
        ResultActions response = mockMvc.perform(get(endpoint + id, imageId));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", org.hamcrest.Matchers.is((int) imageId)))
                .andExpect(jsonPath("$.name").value(image.getName()))
                .andExpect(jsonPath("$.url").value(image.getUrl()));
    }
}
