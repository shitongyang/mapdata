package iscas.stategrid.mapdata.websocket;

import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author : lvxianjin
 * @Date: 2019/10/24 08:56
 * @Description: 后台主动发送语音信息
 */
@ServerEndpoint("/ZTSocket")
@Controller
public class ZTSocket {
    /**
     * session 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * webSocketSet concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<ZTSocket> webSocketSet = new CopyOnWriteArraySet();

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
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
    }

    /**
     * 收到节点信息
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        for (ZTSocket item : webSocketSet) {
            item.sendMessage(message);
        }
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
        for (ZTSocket socketServer : webSocketSet) {
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
}