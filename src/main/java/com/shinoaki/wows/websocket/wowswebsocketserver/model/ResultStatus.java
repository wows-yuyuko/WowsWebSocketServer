package com.shinoaki.wows.websocket.wowswebsocketserver.model;

/**
 * @author Xun
 * @date 2023/5/4 18:31 星期四
 */
public enum ResultStatus {
    SUCCESS(200, "success"),
    ID_ERROR(501, "id not null");;

    private final int code;
    private final String message;

    ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
