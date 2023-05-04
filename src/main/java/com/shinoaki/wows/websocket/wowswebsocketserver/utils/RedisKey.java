package com.shinoaki.wows.websocket.wowswebsocketserver.utils;

/**
 * @author Xun
 * @date 2023/5/4 20:41 星期四
 */
public class RedisKey {
    private RedisKey(){}
    public static String report(String key) {
        return "wows-data:report:" + key;
    }
}
