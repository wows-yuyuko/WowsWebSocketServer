package com.shinoaki.wows.websocket.wowswebsocketserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
public class WowsWebSocketServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WowsWebSocketServerApplication.class, args);
    }

}
