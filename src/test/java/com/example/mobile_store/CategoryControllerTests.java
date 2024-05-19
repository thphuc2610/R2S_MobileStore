package com.example.mobile_store;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.mobile_store.dto.CategoryDTO;
import com.example.mobile_store.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Ignore spring security
public class CategoryControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private final String endpoint = "/api/category";
    private final String id = "/{id}";

    @Test
    public void getAllCategories() throws Exception {
        // given
        List<CategoryDTO> categories = new ArrayList<>();
        CategoryDTO category1 = new CategoryDTO();
        category1.setId(1L);
        category1.setName("Category 1");

        CategoryDTO category2 = new CategoryDTO();
        category2.setId(2L);
        category2.setName("Category 2");

        categories.add(category1);
        categories.add(category2);

        given(categoryService.findAll()).willReturn(categories);

        // when
        ResultActions response = mockMvc.perform(get(endpoint));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", org.hamcrest.Matchers.is(categories.size())));
    }

    @Test
    public void getById() throws Exception {
        // given
        long categoryId = 1L;
        CategoryDTO category = new CategoryDTO();
        category.setId(categoryId);
        category.setName("Category 1");
        given(categoryService.findById(categoryId)).willReturn(category);

        // when
        ResultActions response = mockMvc.perform(get(endpoint + id, categoryId));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", org.hamcrest.Matchers.is((int) categoryId)))
                .andExpect(jsonPath("$.name", org.hamcrest.Matchers.is(category.getName())));
    }

    @Test
    public void createCategory() throws Exception {
        // given
        CategoryDTO newCategory = new CategoryDTO();
        newCategory.setName("New Category");
        given(categoryService.create(any(CategoryDTO.class))).willReturn(newCategory);

        // when
        ResultActions response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newCategory)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", org.hamcrest.Matchers.is(newCategory.getName())));
    }

    @Test
    public void updateCategory() throws Exception {
        // given
        long categoryId = 1L;
        CategoryDTO updatedCategory = new CategoryDTO();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("Updated Category");
        given(categoryService.update(anyLong(), any(CategoryDTO.class))).willReturn(updatedCategory);

        // when
        ResultActions response = mockMvc.perform(put(endpoint + id, categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedCategory)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", org.hamcrest.Matchers.is((int) categoryId)))
                .andExpect(jsonPath("$.name", org.hamcrest.Matchers.is(updatedCategory.getName())));
    }

    @Test
    public void deleteCategory() throws Exception {
        // given
        long categoryId = 1L;
        willDoNothing().given(categoryService).delete(categoryId);

        // when
        ResultActions response = mockMvc.perform(delete(endpoint + id, categoryId));

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
