package com.byritium.message.domain.account.entity;


public class AccountInfo {
    private final String key = "ACCOUNT-INFO";
    private String username;
    private String password;
    private String token;


    public String getKey() {
        return key + ":" + username;
    }

    public boolean validate(String password) {
        return this.password.equals(password);
    }
}
