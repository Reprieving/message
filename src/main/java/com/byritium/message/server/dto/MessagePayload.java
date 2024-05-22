package com.byritium.message.server.dto;


public class MessagePayload {
    private IdAuth idAuth;
    private MessageSubject subject;
    private SendParam sendParam;

    public IdAuth getIdAuth() {
        return idAuth;
    }

    public void setIdAuth(IdAuth idAuth) {
        this.idAuth = idAuth;
    }

    public MessageSubject getSubject() {
        return subject;
    }

    public void setSubject(MessageSubject subject) {
        this.subject = subject;
    }

    public SendParam getSendParam() {
        return sendParam;
    }

    public void setSendParam(SendParam sendParam) {
        this.sendParam = sendParam;
    }
}
