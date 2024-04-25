package com.byritium.message.domain.account.entity;

import lombok.Data;

@Data
public class AccountInfo {
    private final String key = "ACCOUNT-INFO";
    private String username;
    private String password;

    public String getKey() {
        return key + ":" + username;
    }
}
