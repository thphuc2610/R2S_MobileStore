package com.example.mobile_store.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mobile_store.dto.LoginDTO;
import com.example.mobile_store.dto.RegisterDTO;
import com.example.mobile_store.dto.UserDTO;
import com.example.mobile_store.entity.Role;
import com.example.mobile_store.entity.User;
import com.example.mobile_store.exception.UserNotFoundException;
import com.example.mobile_store.exception.NotFoundException;
import com.example.mobile_store.mapper.UserMapper;
import com.example.mobile_store.repository.RoleRepository;
import com.example.mobile_store.repository.UserRepository;
import com.example.mobile_store.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    // Tao User
    public RegisterDTO saveUser(RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new UserNotFoundException("Tên người dùng đã tồn tại.");
        }

        Role role = roleRepository.findById(registerDTO.getRole())
                .orElseThrow(() -> new NotFoundException(registerDTO.getRole()));

        User user = userMapper.toEntity(registerDTO);
        user.setPassword((passwordEncoder.encode(user.getPassword())));
        user.setRole(role);
        return userMapper.toDTO(userRepository.save(user));
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserDTO> getAllUser() throws NotFoundException {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("Không có người dùng nào.");
        }
        return users.stream().map(userMapper::toUserDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        return userMapper.toUserDTO(user);
    }

    @Override
    public void deleteUserById(Long id){
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public Object login(LoginDTO any) {
        return null;
    }
}
