package com.example.mobile_store.dto;

import lombok.Data;

@Data
public class ResponseLoginDTO {
    private String username;
    private String token;
    private String role;
}
