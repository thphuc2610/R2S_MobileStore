package com.example.mobile_store.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.example.mobile_store.dto.RoleDTO;
import com.example.mobile_store.entity.Role;

@Mapper(componentModel = "spring")
@Component
public interface RoleMapper {
    Role toEntity(RoleDTO roleDTO);

    RoleDTO toDTO(Role role);
}
