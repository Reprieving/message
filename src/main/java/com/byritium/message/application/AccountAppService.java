package com.byritium.message.application;

import com.byritium.message.domain.account.entity.AccountInfo;
import com.byritium.message.domain.account.repository.AccountRepository;
import com.byritium.message.domain.account.repository.SessionRepository;
import com.byritium.message.server.dto.IdAuth;
import com.byritium.message.server.dto.MessagePayload;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountAppService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void create() {
    }

    public void message(MessagePayload messagePayload, Channel channel) {
        IdAuth idAuth = messagePayload.getIdAuth();
        String username = idAuth.getUsername();
        String password = idAuth.getUsername();
        String identifier = idAuth.getIdentifier();
        AccountInfo accountInfo = accountRepository.auth(username, password);

        sessionRepository.save(username, identifier, channel.id());

        kafkaTemplate.send("", "");
    }

}
