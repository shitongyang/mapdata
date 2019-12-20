package iscas.stategrid.mapdata.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import iscas.stategrid.mapdata.service.dc_lineService;
import iscas.stategrid.mapdata.util.RedisClient;
import iscas.stategrid.mapdata.util.StaticResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;

import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/WS")
@Component
public class MapTopoWebSocket {
    private static dc_lineService dc_lineService;
    /**
     * session 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    private  String str="";
    //保存前端传过来的值
    private static MyThread1 a=null;

    /**
     * webSocketSet concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<MapTopoWebSocket> webSocketSet = new CopyOnWriteArraySet();

    @Autowired
    public void setdc_lineService(dc_lineService dc_lineService) {

        MapTopoWebSocket.dc_lineService = dc_lineService;
    }
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println(session.getId());
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
        if(a!=null){
          a.stop();
        }
        RedisClient client = new RedisClient();
        client.setValue("id","全国,1");
    }
    /**
     * 收到节点信息
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public  void onMessage(String message) {
        System.out.println("WS的socket传过来的参数是"+message);
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
        //RedisClient client = new RedisClient();
        //client.setValue("id",message);
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
                List<Map<String, Object>> topo_Line_info = dc_lineService.getTopoLine(name);
                List<Map<String, Object>> topo_Location_info = dc_lineService.getTopoLocation(name);
                Map<String,Object> result = resultMap(topo_Line_info, topo_Location_info, name);
                System.out.println("在run方法中进来的参数是:" + name);
                JSONObject object=JSONObject.parseObject(name);
                String quyu=object.getString("area");
                String isStatic=object.getString("JZStatus");
                result.put("hide","false");
                //地图是否隐藏，false的话不隐藏，ture的话隐藏
                if(quyu.equals("全国")&&isStatic.equals("2")){
                    Map<String,Object> error_map = new HashMap<>();
                    error_map.put("Flng", "106.23849358740017");
                    error_map.put("Flat", "38.492460055509596");
                    error_map.put("Tlng", "117.33611995705515");
                    error_map.put("Tlat", "23.849355608251166");
                    error_map.put("percent","0.3");
                    List<Map<String,Object>> badPointList=new ArrayList();
                    badPointList.add(error_map);

                    Map<String,Object> error_line = new HashMap<>();
                    error_line.put("Flng", "106.23849358740017");
                    error_line.put("Flat", "38.492460055509596");
                    error_line.put("Tlng", "117.33611995705515");
                    error_line.put("Tlat", "23.849355608251166");
                    List<Map<String,Object>> badLineList=new ArrayList();
                    badLineList.add(error_line);
                    result.put("badPoint",badPointList);
                    result.put("badLine",badLineList);
                }
                else if(StaticResource.AREA_Set.contains(quyu)&&"2".equals(isStatic)){

                    System.out.println("已经进入区域暂态");
                    System.out.println(result);
                    result.remove("area_tpLine");
                    result.remove("area_tpLocation");
                    List<Map<String,Object>> nullList1=new ArrayList<>();
                    List<Map<String,Object>> nullList2=new ArrayList<>();
                    result.put("area_tpLine",nullList1);
                    result.put("area_tpLocation",nullList2);
                    //把拓扑的站点和线路清空
                    String error_point=object.getString("error_point");
                    Map mapTypes = JSON.parseObject(error_point);
                    List<Map<String,Object>> badPointList=new ArrayList();
                    badPointList.add(mapTypes);

                    String error_line=object.getString("error_line");
                    List listTypes=JSONObject.parseArray(error_line);
                    List<Map<String,Object>> badLineList=listTypes;
                    result.put("badPoint",badPointList);
                    result.put("badLine",badLineList);
                    result.put("hide","true");
                }
                sendMessage(JSON.toJSONString(result));
                try {
                    if("2".equals(isStatic)){
                        Thread.sleep(1000);
                    }
                    else {
                        Thread.sleep(5000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
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
    public  void  sendMessage(String message) {
       for (MapTopoWebSocket socketServer : webSocketSet) {
            try {
               // synchronized (session) {
                socketServer.session.getBasicRemote().sendText(message);
                //}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String,Object> resultMap(List<Map<String,Object>> list1,List<Map<String,Object>> list2,String message){

         String line="";
         String location="";
        JSONObject object=JSONObject.parseObject(message);
        String area=object.getString("area");
        Map<String,Object> map=new HashMap<>();
        if("全国".equals(area)){
            line="china_tpLine";
            location="china_tpLocation";
        }
        else if (StaticResource.AREA_Set.contains(area)){
            line="area_tpLine";
            location="area_tpLocation";
        }
        else if(StaticResource.PROVINCE_Set.contains(area)){
            line="province_tpLine";
            location="province_tpLocation";
        }
        else if(StaticResource.CITY_SET.contains(area)){
            line="city_tpLine";
            location="city_tpLocation";
        }
        else
        {
            line="error";
            location="error";
        }
        map.put(line,list1);
        map.put(location,list2);
        return map;
    }
    public Session getSession() {
        return session;
    }
    public void setSession(Session session) {
        this.session = session;
    }
}
