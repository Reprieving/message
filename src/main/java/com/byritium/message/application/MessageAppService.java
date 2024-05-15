package com.byritium.message.application;

import com.byritium.message.domain.account.entity.AccountInfo;
import com.byritium.message.domain.account.repository.AccountRepository;
import com.byritium.message.domain.account.repository.SessionRepository;
import com.byritium.message.server.dto.IdAuth;
import com.byritium.message.server.dto.MessagePayload;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MessageAppService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public void send2Server(MessagePayload messagePayload, Channel channel) {
        IdAuth idAuth = messagePayload.getIdAuth();
        String username = idAuth.getUsername();
        String password = idAuth.getUsername();
        String identifier = idAuth.getIdentifier();
        AccountInfo accountInfo = accountRepository.findByUserName(username);
        if (accountInfo == null || !accountInfo.validate(password)) {
            throw new RuntimeException("auth fail");
        }


    }

    public void send2Client() {

    }


}
