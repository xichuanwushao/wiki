package com.xichuan.wiki.service;

import com.xichuan.wiki.websocket.WebSocketServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class WsService {
    @Resource
    public WebSocketServer webSocketServer;
    @Async
    public void sendInfo(String info){
        webSocketServer.sendInfo(info);
    }
}
