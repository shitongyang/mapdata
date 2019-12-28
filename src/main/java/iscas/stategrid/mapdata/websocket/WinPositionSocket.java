package iscas.stategrid.mapdata.websocket;


import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.service.KongJService;
import iscas.stategrid.mapdata.service.WinPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/Win")
@Component
public class WinPositionSocket {
    private MyThread1 thread1 = new MyThread1();
    private static WinPositionService winPositionService;
    @Autowired
    public void setJCInfoService(WinPositionService winPositionService) {
        WinPositionSocket.winPositionService = winPositionService;
    }
    /**
     * session 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * webSocketSet concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<WinPositionSocket> webSocketSet = new CopyOnWriteArraySet();

    /**
     * 功能描述: websocket 连接建立成功后进行调用
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/22 19:29
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        System.out.println("风电场Socket连接成功");
        thread1.start();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        System.out.println("风电场Socket连接关闭");
        thread1.stop();
    }

    /**
     * 收到节点信息
     *
     *形式参数的message
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {

    }

    /**
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public synchronized void sendMessage(String message) {
        for (WinPositionSocket socketServer : webSocketSet) {
            try {
                //synchronized (session) {
                socketServer.session.getBasicRemote().sendText(message);
                //}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    public  class MyThread1 extends Thread
    {
        public String name;
        public boolean isRun=true;
        @Override
        public void run()
        {
            while(isRun) {
                List<Map<String,String>> DYW_info = winPositionService.getDYWInfo();
                sendMessage(JSON.toJSONString(DYW_info));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
