package iscas.stategrid.mapdata.websocket;

import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.service.KongJService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author : lvxianjin
 * @Date: 2019/10/26 18:37
 * @Description:
 */
@ServerEndpoint("/KJ")
@Controller
public class ControlSocket {
    private static KongJService jcInfoService;
    /**
     * session 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * webSocketSet concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<ControlSocket> webSocketSet = new CopyOnWriteArraySet();
    @Autowired
    public void setJCInfoService(KongJService jcInfoService) {
        ControlSocket.jcInfoService = jcInfoService;
    }
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
        System.out.println("区域Socket连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        System.out.println("区域Socket连接关闭");
    }
    /**
     * 收到节点信息
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        System.out.println("KJ的socket传来的参数是"+message);
        Map<String,Object> area_info = jcInfoService.getKongJInfo(message);
        sendMessage(JSON.toJSONString(area_info));
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
    public void sendMessage(String message) {
        for (ControlSocket socketServer : webSocketSet) {
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
        boolean isStop=true;
        public void terminate(){
            this.isStop=false;
        }

        public boolean isRun=true;
        MyThread1 (String name){
            this.name=name;
        }
        @Override
        public void run()
        {
            while(isRun) {
                Map<String,Object> area_info = jcInfoService.getKongJInfo(name);
                sendMessage(JSON.toJSONString(area_info));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
