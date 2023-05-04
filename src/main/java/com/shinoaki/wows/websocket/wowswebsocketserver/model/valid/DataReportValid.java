package com.shinoaki.wows.websocket.wowswebsocketserver.model.valid;

import java.util.List;

/**
 * @author Xun
 * @date 2023/5/4 18:43 星期四
 */
public record DataReportValid(String battleType, int mapId, long battleTime, List<User> userList) {

    public record User(Clan clan, String server, String userName, long shipId, long accountId, int relation) {

    }

    public record Clan(long clanId, String tag) {

    }
}
