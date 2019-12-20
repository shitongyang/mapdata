package iscas.stategrid.mapdata.websocket;

import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.service.dc_lineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/WSS")
@Component
public class GlobalTopoWebSocket {
    static  String str="";
    static  MyThread a=null;
//    public void setStr(String str){
//        GlobalTopoWebSocket.str=str;
//    }
//    public void setMyThread(MyThread thread){
//
//        GlobalTopoWebSocket.a=thread;
//    }

    private static dc_lineService dc_lineService;
    /**
     * session 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * webSocketSet concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<GlobalTopoWebSocket> webSocketSet = new CopyOnWriteArraySet();

    @Autowired
    public void setdc_lineService(dc_lineService dc_lineService) {
        GlobalTopoWebSocket.dc_lineService = dc_lineService;
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
        //获取拓扑结构
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

        System.out.println("客户端传输的mesage是"+message);
        //获取拓扑结构
        /*List<Map<String,Object>> topo_info = dc_lineService.getTopoLine(message);
        Map<String,List<Map<String,Object>>> result=new HashMap<>();
        result.put("topo",topo_info);
        sendMessage(JSON.toJSONString(result));*/

        if(str=="") {
            str = message;
            a= new MyThread(message);
            a.start();
        }
        if(str!=message){
            a.stop();
            a= new MyThread(message);
            a.start();
            str=message;
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
    public static void sendMessage(String message) {
        for (GlobalTopoWebSocket socketServer : webSocketSet) {
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
    public static class MyThread extends Thread{

        String name;
        final long timeInterval = 1000;
        MyThread (String name){
            this.name=name;
        }
        @Override
        public void run(){
            while(true) {
                try {
                    Thread.sleep(timeInterval);
                    long start=System.currentTimeMillis();
                    System.out.println(this.name);
                    List<Map<String,Object>> topo_info = dc_lineService.getTopoLine(this.name);
                    Map<String,List<Map<String,Object>>> result=new HashMap<>();
                    result.put("topo",topo_info);
                    sendMessage(JSON.toJSONString(result));
                    Long end =System.currentTimeMillis();
                    System.out.println("共用时："+(end-start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
