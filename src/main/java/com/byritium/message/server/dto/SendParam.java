package com.byritium.message.server.dto;

import com.byritium.message.constance.SendType;
import lombok.Data;

@Data
public class SendParam {
    private SendType sendType;
    private String targetId;
}
