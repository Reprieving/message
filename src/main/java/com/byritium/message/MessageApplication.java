package com.byritium.message;

import com.byritium.message.server.NettyServer;

public class MessageApplication {

    public static void main(String[] args) throws InterruptedException {
        new NettyServer().startup();
    }

}
