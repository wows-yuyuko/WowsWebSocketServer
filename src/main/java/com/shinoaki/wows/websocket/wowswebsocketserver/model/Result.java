package com.shinoaki.wows.websocket.wowswebsocketserver.model;

import com.shinoaki.wows.websocket.wowswebsocketserver.utils.DateUtils;

/**
 * @author Xun
 * @date 2023/5/4 18:30 星期四
 */
public record Result<T>(int code, String message, T data, long time) {

    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMessage(), data, DateUtils.toEpochMilli());
    }

    public static <T> Result<T> status(ResultStatus status, T data) {
        return new Result<>(status.getCode(), status.getMessage(), data, DateUtils.toEpochMilli());
    }
}
