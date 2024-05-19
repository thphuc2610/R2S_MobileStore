package com.example.mobile_store.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.mobile_store.constant.ApiUrlConstant;
import com.example.mobile_store.dto.LoginDTO;
import com.example.mobile_store.dto.RegisterDTO;
import com.example.mobile_store.dto.ResponseLoginDTO;
import com.example.mobile_store.dto.UserDTO;
import com.example.mobile_store.exception.UserNotFoundException;
import com.example.mobile_store.service.UserService;
import com.example.mobile_store.util.TokenProvider;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(ApiUrlConstant.USER)

public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService,
            AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(ApiUrlConstant.REGISTER)
    public ResponseEntity<RegisterDTO> register(@RequestBody RegisterDTO registerDTO) {
        RegisterDTO user = userService.saveUser(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping(ApiUrlConstant.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                            loginDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                ResponseLoginDTO responseLoginDTO = new ResponseLoginDTO();
                responseLoginDTO.setUsername(loginDTO.getUsername());
                responseLoginDTO.setToken("Bearer " + TokenProvider.generateToken(loginDTO.getUsername()));

                List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .toList();
                responseLoginDTO.setRole(roles.get(0));
                return new ResponseEntity<>(responseLoginDTO, HttpStatus.OK);
            }
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("Sai tên đăng nhập hoặc mật khẩu");
        }
        return null;
    }

    @GetMapping(ApiUrlConstant.READ)
    public ResponseEntity<List<UserDTO>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping(ApiUrlConstant.READ_ID)
    public ResponseEntity<?> findById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(userService.findById(id));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(ApiUrlConstant.DELETE_ID)
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
