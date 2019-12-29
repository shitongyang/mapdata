package iscas.stategrid.mapdata.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import iscas.stategrid.mapdata.service.MapService;
import iscas.stategrid.mapdata.Utils.StaticResource;
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
    private static MapService mapService;
    /**
     * session 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    private static volatile String str="";
    //保存前端传过来的值
    private static MyThread a=null;

    /**
     * webSocketSet concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<MapTopoWebSocket> webSocketSet = new CopyOnWriteArraySet();

    @Autowired
    public void setdc_lineService(MapService mapService) {
        MapTopoWebSocket.mapService = mapService;
    }
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        System.out.println("地图Socket连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        System.out.println("地图Socket连接关闭");
        if(a!=null){
          a.stop();
        }
//        RedisClient client = new RedisClient();
//        client.setValue("id","全国,1");
    }
    /**
     * 收到节点信息
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public  void onMessage(String message) {
        System.out.println("地图_的socket传过来的参数是"+message);
        try {
            JSONObject object = JSONObject.parseObject(message);
        }catch (Exception e){
            sendMessage(JSONObject.toJSONString(StaticResource.jsonErrorResult(message)));
            System.out.println("前端传过来的参数不符合json格式");
            return;
        }
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
    public  class MyThread extends Thread
    {

        public String name;
        public boolean isRun=true;
        MyThread (String name){
            this.name=name;
        }
        @Override
        public void run()
        {
            while(isRun) {
                System.out.println("在地图_run方法中进来的参数是:" + name);
                JSONObject object=JSONObject.parseObject(name);
                List<Map<String, Object>> topo_Line_info = mapService.getTopoLine(name);
                List<Map<String, Object>> topo_Location_info = mapService.getTopoLocation(name);
                Map<String,Object> result = resultMap(topo_Line_info, topo_Location_info, name);

                String quyu=object.getString("area");
                String isStatic=object.getString("JZStatus");
                result.put("hide","false");
                //地图是否隐藏，false的话不隐藏，ture的话隐藏
                if(quyu.equals("全国")&&isStatic.equals("2")){
                    Map<String,Object> error_map = new HashMap<>();
                    error_map.put("Flng", "111.04057");
                    error_map.put("Flat", "30.832745");
                    error_map.put("Tlng", "111.456515837124");
                    error_map.put("Tlat", "30.38440427431352");
                    error_map.put("percent","0.3");
                    List<Map<String,Object>> badPointList=new ArrayList();
                    badPointList.add(error_map);

                    Map<String,Object> error_line1 = new HashMap<>();
                    error_line1.put("Flng", "111.04057");
                    error_line1.put("Flat", "30.832745");
                    error_line1.put("Tlng", "111.456515837124");
                    error_line1.put("Tlat", "30.38440427431352");

                    Map<String,Object> error_line2 = new HashMap<>();
                    error_line2.put("Flng", "111.04057");
                    error_line2.put("Flat", "30.832745");
                    error_line2.put("Tlng", "112.43145201727503");
                    error_line2.put("Tlat", "30.048042984592485");


                    Map<String,Object> error_line3 = new HashMap<>();
                    error_line3.put("Flng", "111.456515837124");
                    error_line3.put("Flat", "30.38440427431352");
                    error_line3.put("Tlng", "114.519578");
                    error_line3.put("Tlat", "34.787032");

                    List<Map<String,Object>> badLineList=new ArrayList();
                    badLineList.add(error_line1);
                    badLineList.add(error_line2);
                    badLineList.add(error_line3);
                    result.put("badPoint",badPointList);
                    result.put("badLine",badLineList);
                }
                else if(StaticResource.AREA_Set.contains(quyu)&&"2".equals(isStatic)){

                    System.out.println("已经进入区域暂态");
                    result.remove("area_tpLine");
                    result.remove("area_tpLocation");
                    //把拓扑的站点和线路清空
                    String error_point=object.getString("error_point");
                    Map mapTypes = JSON.parseObject(error_point);
                    List<Map<String,Object>> badPointList=new ArrayList();
                    badPointList.add(mapTypes);

                    String error_line=object.getString("error_line");
                    List badLineList=JSONObject.parseArray(error_line);
                    result.put("badPoint",badPointList);
                    result.put("badLine",badLineList);
                    String bj_line=object.getString("bj_line");
                    List bj_List=JSONObject.parseArray(bj_line);
                    result.put("bj_line",bj_List);

                    String bj_point=object.getString("bj_point");
                    List bj_pointList=JSONObject.parseArray(bj_point);
                    result.put("bj_point",bj_pointList);
                    result.put("nameInfo",object.get("nameInfo"));
                    result.put("hide","true");
                }
                else if(quyu.equals("全国")&&isStatic.equals("3")){
                    System.out.println("已经进入全国薄弱点");
                    result.remove("china_tpLine");
                    result.remove("china_tpLocation");
                    String globalWeak=object.getString("weak_Location");
                    List globalWeakList=JSONObject.parseArray(globalWeak);
                    result.put("weak_Location",globalWeakList);
                }
                sendMessage(JSON.toJSONString(result));
                try {
                    if("2".equals(isStatic)){
                        Thread.sleep(1000);
                        //暂态一秒传一次
                    }
                    else {
                        Thread.sleep(5000);
                        //稳态五秒传一次
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
                synchronized (socketServer.session) {
                socketServer.session.getBasicRemote().sendText(message);
                }
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
