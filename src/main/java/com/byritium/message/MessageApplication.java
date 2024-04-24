package com.byritium.message;

import com.byritium.message.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessageApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication app = new SpringApplication(MessageApplication.class);
        app.run(args);
        new NettyServer().startup();
    }

}
