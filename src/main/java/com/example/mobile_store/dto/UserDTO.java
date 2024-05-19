package com.example.mobile_store.dto;

import java.sql.Date;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDTO {
    private long id;

    private String username;

    private String password;
    
    private String name;

    private Date birthDate;

    private boolean gender;

    private String phoneNumber;

    private String email;

    private LocalDateTime createAt;

    private String status;

    private String address;

    private Long role;
}
