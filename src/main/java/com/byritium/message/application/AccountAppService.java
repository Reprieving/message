package com.byritium.message.application;

import com.byritium.message.component.KafkaSender;
import com.byritium.message.component.RedisClient;
import com.byritium.message.constance.MessageTopic;
import com.byritium.message.domain.account.entity.AccountInfo;
import com.byritium.message.domain.account.repository.AccountRepository;
import com.byritium.message.domain.account.repository.SessionRepository;
import com.byritium.message.server.dto.IdAuth;
import com.byritium.message.server.dto.MessagePayload;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountAppService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private KafkaSender kafKaSender;

    @Autowired
    private RedisClient redisClient;

    public void create(AccountInfo accountInfo) {
        redisClient.set(accountInfo.getKey(), accountInfo);
    }

    public void update(AccountInfo accountInfo) {
        redisClient.set(accountInfo.getKey(), accountInfo);
    }

    public void message(MessagePayload messagePayload, Channel channel) {
        IdAuth idAuth = messagePayload.getIdAuth();
        String username = idAuth.getUsername();
        String password = idAuth.getUsername();
        String identifier = idAuth.getIdentifier();

        sessionRepository.save(username, identifier, channel.id());

        String content = messagePayload.getSubject().getContent();
        kafKaSender.send(MessageTopic.SEND_CUSTOMER, content);
        kafKaSender.send(MessageTopic.SEND_TERMINAL, content);
    }

}
