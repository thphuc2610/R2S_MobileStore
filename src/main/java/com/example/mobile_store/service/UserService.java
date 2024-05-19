package com.example.mobile_store.service;

import java.util.List;
import java.util.Optional;

import com.example.mobile_store.dto.LoginDTO;
import com.example.mobile_store.dto.RegisterDTO;
import com.example.mobile_store.dto.UserDTO;
import com.example.mobile_store.entity.User;
import com.example.mobile_store.exception.UserNotFoundException;
import com.example.mobile_store.exception.NotFoundException;

public interface UserService {
    RegisterDTO saveUser(RegisterDTO registerDTO) throws UserNotFoundException, NotFoundException;
    
    Optional<User> getUserByUsername(String username);

    List<UserDTO> getAllUser() throws NotFoundException;

    UserDTO findById(long id);

    void deleteUserById(Long id);

    Object login(LoginDTO any);
}