package com.byritium.message.server.dto;

import lombok.Data;

@Data
public class TcpPayload {
    private int len;
    private byte[] content;
}
