package com.byritium.message.server.dto;

import lombok.Data;

@Data
public class IdAuth {
    private String username;
    private String password;
    private String identifier;
}
