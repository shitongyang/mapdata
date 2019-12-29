package iscas.stategrid.mapdata.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import iscas.stategrid.mapdata.service.KongJService;
import iscas.stategrid.mapdata.service.MapService;
import iscas.stategrid.mapdata.Utils.StaticResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author : lvxianjin
 * @Date: 2019/10/26 18:37
 * @Description:
 */
@ServerEndpoint("/KJ")
@Controller
public class ControlSocket {
    private static KongJService KongJService;

    private static MapService mapService;
    /**
     * session 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    private static List<Map<String,Object>> list=new ArrayList<>();
    private static String value=null;
    //散点图


    private  static volatile String str="";
    //保存前端传过来的值
    private static MyThread1 a=null;
    /**
     * webSocketSet concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<ControlSocket> webSocketSet = new CopyOnWriteArraySet();
    @Autowired
    public void setMapService(MapService mapService) {
        ControlSocket.mapService = mapService;
    }
    @Autowired
    public void setJCInfoService(KongJService jcInfoService) {
        ControlSocket.KongJService = jcInfoService;
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
        System.out.println("控件Socket连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        if(a!=null)
            a.stop();
        System.out.println("控件Socket连接关闭");
    }
    /**
     * 收到节点信息
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        System.out.println("控件_的socket传来的参数是"+message);
        try {
            JSONObject object = JSONObject.parseObject(message);
        }catch (Exception e){
            sendMessage(JSONObject.toJSONString(StaticResource.jsonErrorResult(message)));
            System.out.println("前端传过来的参数不符合json格式");
            return;
        }
        if(str=="") {
            str = message;
            a= new MyThread1(message);
            a.start();
        }
        if(str!=message){
            a.stop();
            a= new MyThread1(message);
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
    public void sendMessage(String message) {
        for (ControlSocket socketServer : webSocketSet) {
            try {
                synchronized (session) {
                socketServer.session.getBasicRemote().sendText(message);
                }
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

    public List<Map<String,Object>> sanDianMap(){

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        if(list.size()<=10){
                Map<String, Object> map = new HashMap<>();
                map.put("time", df.format(new Date()));
                if(getValue()==null) {
                    map.put("value", "1");
                }
                else{
                    map.put("value",getValue());
                    setValue(null);
                }
                list.add(map);
        }
        else
        {
            list.remove(0);
        }

        return list;
    }


    public  class MyThread1 extends Thread
    {

        public String name;
        public boolean isRun=true;
        MyThread1 (String name){
            this.name=name;
        }
        @Override
        public void run()
        {
            while(isRun) {
                System.out.println("在控件_run方法中进来的参数是:" + name);
                List<Map<String, Object>> topo_Line_info = mapService.getTopoLine(name);
                Map<String,Object> data6=new HashMap<>();
                //
                data6.put("left1",topo_Line_info.size());
                data6.put("left2",(int)(Math.random()*10)+20);
                data6.put("right1",(int)(Math.random()*2)+5);
                //Calendar calendar=Calendar.getInstance();
                //int currentHour24=calendar.get(calendar.HOUR_OF_DAY);
                data6.put("right2",(int)(Math.random()*8)+1+"h");
                Map<String,Object> area_info = KongJService.getKongJInfo(name);
                area_info.put("data6",data6);
                area_info.put("data10",sanDianMap());
                sendMessage(JSON.toJSONString(area_info));
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
