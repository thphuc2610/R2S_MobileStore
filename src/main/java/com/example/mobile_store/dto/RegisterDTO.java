package com.example.mobile_store.dto;

import java.sql.Date;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;

    private String password;

    private String name;

    private Date birthDate;

    private String gender;

    private String phoneNumber;

    private String email;

    private String address;

    private Long role;

    private LocalDateTime createAt;
}
