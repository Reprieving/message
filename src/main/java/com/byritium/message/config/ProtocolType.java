package com.byritium.message.config;

public enum ProtocolType {
    TCP("TCP"),
    UDP("UDP"),
    HTTP("HTTP"),
    MQTT("MQTT"),
    COAP("COAP");
    private final String type;

    ProtocolType(String tcp) {
        this.type = tcp;
    }

}
