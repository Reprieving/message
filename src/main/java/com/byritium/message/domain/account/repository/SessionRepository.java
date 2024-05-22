package com.byritium.message.domain.account.repository;

import com.byritium.message.domain.account.entity.AccountInfo;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class SessionRepository {
    private static final ConcurrentHashMap<String, Channel> map = new ConcurrentHashMap<>();

    public void save(String username, String identifier, Channel channel) {
        map.putIfAbsent(username + "-" + identifier, channel);
    }

    public Channel get(String username, String identifier) {
        return map.get(username + "-" + identifier);
    }
}
