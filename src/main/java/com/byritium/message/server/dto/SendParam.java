package com.byritium.message.server.dto;

import com.byritium.message.constance.SendType;

public class SendParam {
    private SendType sendType;
    private String targetId;

    public SendType getSendType() {
        return sendType;
    }

    public void setSendType(SendType sendType) {
        this.sendType = sendType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
