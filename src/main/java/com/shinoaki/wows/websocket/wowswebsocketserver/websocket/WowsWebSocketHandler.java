package com.shinoaki.wows.websocket.wowswebsocketserver.websocket;

import com.shinoaki.wows.websocket.wowswebsocketserver.utils.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Xun
 * @date 2023/5/4 18:51 星期四
 */
@Component
public class WowsWebSocketHandler implements WebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(WowsWebSocketHandler.class);
    private static final ConcurrentMap<String, Set<WebSocketSession>> SESSION_MAP = new ConcurrentHashMap<>();

    private final StringRedisTemplate stringRedisTemplate;

    public WowsWebSocketHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public static void sendMessage(String key, String message) {
        for (WebSocketSession session : SESSION_MAP.getOrDefault(key, Set.of())) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.info("key={} 用户id={} 发送失败!", key, session.getId(), e);
            }
        }
    }

    /**
     * 处理新的WebSocket连接
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //新的连接
        log.info("new websocket connect id={}", session.getId());
    }

    /**
     * 处理收到的WebSocket消息
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage textMessage) {
            String payload = textMessage.getPayload();
            if (payload.startsWith("[ping:") && payload.endsWith("]")) {
                String key = payload.substring(6, payload.length() - 1);
                SESSION_MAP.computeIfAbsent(key, set -> new HashSet<>()).add(session);
                session.sendMessage(message);
                //开始推送消息
                String data = this.stringRedisTemplate.opsForValue().get(RedisKey.report(key));
                if (data != null && !data.isBlank()) {
                    sendMessage(key, data);
                }
            } else {
                log.info("收到文本消息:{}", textMessage);
                session.sendMessage(new TextMessage("[error:message type error !]"));
                session.close();
            }
        } else {
            throw new IllegalStateException("Unexpected WebSocket message type: " + message);
        }
    }


    /**
     * 处理WebSocket传输错误
     *
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("websocket handleTransportError session={}", session.getId(), exception);
        session.close();
    }

    /**
     * 处理WebSocket连接关闭
     *
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        for (var entry : SESSION_MAP.entrySet()) {
            Optional<WebSocketSession> optional = entry.getValue().stream().filter(v -> v.getId().equals(session.getId())).findFirst();
            optional.ifPresent(webSocketSession -> entry.getValue().remove(webSocketSession));
            if (entry.getValue().isEmpty()) {
                SESSION_MAP.remove(entry.getKey());
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
