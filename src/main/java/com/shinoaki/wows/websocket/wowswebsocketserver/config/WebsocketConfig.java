package com.shinoaki.wows.websocket.wowswebsocketserver.config;

import com.shinoaki.wows.websocket.wowswebsocketserver.websocket.WowsWebSocketHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author Xun
 * @date 2023/5/4 18:50 星期四
 */
@Configuration
public class WebsocketConfig implements WebSocketConfigurer {
    @Value("${wows.socket.path}")
    public String socketPath;

    private final WowsWebSocketHandler wowsWebSocketHandler;

    public WebsocketConfig(WowsWebSocketHandler wowsWebSocketHandler) {
        this.wowsWebSocketHandler = wowsWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.wowsWebSocketHandler, socketPath)
                .setAllowedOrigins("*");
//                .addInterceptors();
    }
}
