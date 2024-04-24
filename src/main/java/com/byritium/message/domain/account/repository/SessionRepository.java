package com.byritium.message.domain.account.repository;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepository {
    public void save(String username, String identifier, ChannelId channelId) {

    }
}
