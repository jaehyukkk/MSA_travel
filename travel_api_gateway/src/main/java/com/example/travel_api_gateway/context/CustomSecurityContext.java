package com.example.travel_api_gateway.context;

import lombok.Builder;
import lombok.Data;

@Data
public class CustomSecurityContext {
    private Long id;
    private String username;
    private String roles;

    @Builder
    public CustomSecurityContext(Long id, String username, String roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
