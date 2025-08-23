package com.example.ecommerce_spring_react.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String senha;
}
