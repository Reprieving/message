package com.byritium.message.server.dto;

import lombok.Data;

@Data
public class MessagePayload {
    private IdAuth idAuth;
    private MessageSubject subject;
    private SendParam sendParam;
}
