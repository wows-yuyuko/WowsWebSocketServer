package com.shinoaki.wows.websocket.wowswebsocketserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shinoaki.wows.websocket.wowswebsocketserver.model.Result;
import com.shinoaki.wows.websocket.wowswebsocketserver.model.ResultStatus;
import com.shinoaki.wows.websocket.wowswebsocketserver.model.valid.DataReportValid;
import com.shinoaki.wows.websocket.wowswebsocketserver.utils.JsonUtils;
import com.shinoaki.wows.websocket.wowswebsocketserver.utils.RedisKey;
import com.shinoaki.wows.websocket.wowswebsocketserver.websocket.WowsWebSocketHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Xun
 * @date 2023/5/4 18:37 星期四
 */
@RestController
@RequestMapping(value = "/wows/real/battle/report/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class WowsRealBattleReportController {
    public static final Logger log = LoggerFactory.getLogger(WowsRealBattleReportController.class);
    private final StringRedisTemplate stringRedisTemplate;


    public WowsRealBattleReportController(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 数据上报接口
     *
     * @return 上报数据
     */
    @PostMapping(value = "data")
    public Result<String> dataReport(HttpServletRequest request, @RequestBody DataReportValid valid) throws JsonProcessingException {
        JsonUtils jsonUtils = new JsonUtils();
        String header = request.getHeader("wows-client-id");
        if (header != null && !header.isBlank()) {
            String json = jsonUtils.toJson(valid);
            ValueOperations<String, String> ops = this.stringRedisTemplate.opsForValue();
            ops.set(RedisKey.report(header), json, 20, TimeUnit.MINUTES);
            WowsWebSocketHandler.sendMessage(header, json);
            return Result.ok(null);
        }
        return Result.status(ResultStatus.ID_ERROR, null);
    }
}
