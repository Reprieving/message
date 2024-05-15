package com.byritium.message.domain.account.repository;

import com.byritium.message.component.RedisClient;
import com.byritium.message.domain.account.entity.AccountInfo;
import com.byritium.message.exception.AccountAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountRepository {
    @Autowired
    private RedisClient redisClient;

    private static final ConcurrentHashMap<String, AccountInfo> map = new ConcurrentHashMap<>();

    public AccountInfo findByUserName(String username) {
        AccountInfo accountInfo = map.get(username);
        if (accountInfo == null) {
            accountInfo = (AccountInfo) redisClient.get(username);
            map.putIfAbsent(username, accountInfo);
        }
        return accountInfo;
    }
}
