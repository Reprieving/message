package com.byritium.message.domain.account.repository;

import com.byritium.message.component.RedisClient;
import com.byritium.message.domain.account.entity.AccountInfo;
import com.byritium.message.exception.AccountAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {
    @Autowired
    private RedisClient redisClient;

    public AccountInfo auth(String username, String password) {
        AccountInfo accountInfo = (AccountInfo) redisClient.get(username);
        if (accountInfo == null || accountInfo.getPassword().equals(password)) {
            throw new AccountAuthException("auth fail");
        }
        return accountInfo;
    }
}
