package org.capstonegrp8.restaurant_management_system.auth;


import lombok.Data;

@Data
public class AuthRequest {

    private String email;
    private String password;
}
