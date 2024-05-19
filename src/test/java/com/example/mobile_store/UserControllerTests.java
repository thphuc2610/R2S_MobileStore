package com.example.mobile_store;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.example.mobile_store.config.AuthFilter;
import com.example.mobile_store.dto.LoginDTO;
import com.example.mobile_store.dto.RegisterDTO;
import com.example.mobile_store.dto.ResponseLoginDTO;
import com.example.mobile_store.dto.UserDTO;
import com.example.mobile_store.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {
        private final MockMvc mockMvc;
        private final ObjectMapper objectMapper;

        @MockBean
        private UserService userService;

        @MockBean
        private AuthFilter authFilter;

        private final String endpoint = "/api/user";
        private final String register = "/register";
        private final String login = "/login";
        private final String id = "/{id}";

        @Autowired
        public UserControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, UserService userService,
                        AuthFilter authFilter) {
                this.mockMvc = mockMvc;
                this.objectMapper = objectMapper;
                this.userService = userService;
                this.authFilter = authFilter;
        }

        @Test
        public void testRegister() throws Exception {
                RegisterDTO registerDTO = new RegisterDTO();
                registerDTO.setUsername("testUser");
                registerDTO.setPassword("testPassword");
                registerDTO.setName("Test User");
                registerDTO.setBirthDate(Date.valueOf(LocalDate.of(2004, 10, 26)));
                registerDTO.setGender("Male");
                registerDTO.setPhoneNumber("1234567890");
                registerDTO.setEmail("test@example.com");
                registerDTO.setAddress("123 Test Street");
                registerDTO.setRole(1L);

                when(userService.saveUser(any(RegisterDTO.class))).thenReturn(registerDTO);

                ResultActions result = mockMvc.perform(post(endpoint + register)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerDTO)));

                result.andExpect(status().isCreated())
                                .andExpect(jsonPath("$.username").value(registerDTO.getUsername()))
                                .andExpect(jsonPath("$.name").value(registerDTO.getName()))
                                .andExpect(jsonPath("$.birthDate").value(registerDTO.getBirthDate().toString()))
                                .andExpect(jsonPath("$.gender").value(registerDTO.getGender()))
                                .andExpect(jsonPath("$.phoneNumber").value(registerDTO.getPhoneNumber()))
                                .andExpect(jsonPath("$.email").value(registerDTO.getEmail()))
                                .andExpect(jsonPath("$.address").value(registerDTO.getAddress()))
                                .andExpect(jsonPath("$.role").value(registerDTO.getRole()));
        }

        @Test
        public void testLogin() throws Exception {
                LoginDTO loginDTO = new LoginDTO();
                loginDTO.setUsername("phuc");
                loginDTO.setPassword("abc123phuc");

                ResponseLoginDTO responseLoginDTO = new ResponseLoginDTO();
                responseLoginDTO.setUsername(loginDTO.getUsername());
                responseLoginDTO.setToken(
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaHVjIiwiaWF0IjoxNzE1NDM0ODM3LCJleHAiOjE3MTU0MzY2Mzd9.S5g-4ofqBH8yOCyrSWE-QW8397tUEtesQGgx1hJj7Ts");

                when(userService.login(any(LoginDTO.class))).thenReturn(responseLoginDTO);

                ResultActions result = mockMvc.perform(post(endpoint + login)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO)));

                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.username").value(loginDTO.getUsername()))
                                .andExpect(jsonPath("$.token").value(Matchers.startsWith("Bearer ")));

        }

        @Test
        public void testGetAllUser() throws Exception {
                UserDTO user1 = new UserDTO();
                user1.setUsername("user1");
                user1.setRole(1L);

                UserDTO user2 = new UserDTO();
                user2.setUsername("user2");
                user2.setRole(2L);

                List<UserDTO> userList = Arrays.asList(user1, user2);

                when(userService.getAllUser()).thenReturn(userList);

                ResultActions result = mockMvc.perform(get(endpoint));

                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(userList.size()))
                                .andExpect(jsonPath("$[0].username").value(user1.getUsername()))
                                .andExpect(jsonPath("$[0].role").value(user1.getRole()))
                                .andExpect(jsonPath("$[1].username").value(user2.getUsername()))
                                .andExpect(jsonPath("$[1].role").value(user2.getRole()));
        }

        @Test
        public void getById() throws Exception {
                long userId = 1L;
                UserDTO user = new UserDTO();
                user.setId(userId);
                user.setUsername("Username 1");
                user.setPassword("123@username");
                user.setName("username");
                user.setBirthDate(Date.valueOf(LocalDate.of(2004, 10, 26)));
                user.setGender(false);
                user.setPhoneNumber("1234567890");
                user.setEmail("username@gmail.com");
                user.setCreateAt(LocalDateTime.now());
                user.setStatus("active");
                user.setAddress("123 Example Street, City, Country");
                user.setRole(1L);

                given(userService.findById(userId)).willReturn(user);

                ResultActions response = mockMvc.perform(get(endpoint + id, userId));

                response.andExpect(status().isOk())
                                .andDo(print())
                                .andExpect(jsonPath("$.id", is((int) user.getId())))
                                .andExpect(jsonPath("$.username", is(user.getUsername())))
                                .andExpect(jsonPath("$.password", is(user.getPassword())))
                                .andExpect(jsonPath("$.name", is(user.getName())))
                                .andExpect(jsonPath("$.birthDate", is(user.getBirthDate().toString())))
                                .andExpect(jsonPath("$.gender", is(user.isGender())))
                                .andExpect(jsonPath("$.phoneNumber", is(user.getPhoneNumber())))
                                .andExpect(jsonPath("$.email", is(user.getEmail())))
                                .andExpect(jsonPath("$.createAt", startsWith(user.getCreateAt().toString().substring(0, 19))))
                                .andExpect(jsonPath("$.status", is(user.getStatus())))
                                .andExpect(jsonPath("$.address", is(user.getAddress())))
                                .andExpect(jsonPath("$.role", is(user.getRole().intValue())));
        }

        

        @Test
        public void testDeleteUser() throws Exception {
                long userId = 1;

                mockMvc.perform(delete(endpoint + id, userId))
                                .andExpect(status().isNoContent());
        }
}