package com.byritium.message.domain.account.repository;

import com.byritium.message.domain.account.entity.AccountInfo;

import java.util.concurrent.ConcurrentHashMap;

public class AccountRepository {

    private static final ConcurrentHashMap<String, AccountInfo> map = new ConcurrentHashMap<>();

    public AccountInfo findByUserName(String username) {
        AccountInfo accountInfo = map.get(username);
        if (accountInfo == null) {
            accountInfo = null;
            map.putIfAbsent(username, accountInfo);
        }
        return accountInfo;
    }
}
