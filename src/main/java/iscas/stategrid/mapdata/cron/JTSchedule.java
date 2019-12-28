package iscas.stategrid.mapdata.cron;

import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.Utils.FileClient;
import iscas.stategrid.mapdata.websocket.JTSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/12/25 10:12
 * @Description:
 */
@Component
@EnableScheduling
public class JTSchedule {
    @Autowired
    private JTSocket jtSocket;
    private int count = 0;
    @Scheduled(fixedRate=2000)
    public void doThing(){
        Map<String,Object> data_map = new HashMap<>();
        FileClient fileClient = new FileClient();
        List<String> A_x = fileClient.getContent("/jar/db/1-x.txt");
        List<String> A_y = fileClient.getContent("/jar/db/1-y.txt");
        List<Map<String,String>> A_line = new ArrayList<>();
        Map<String,String> A_point = new HashMap<>();
        Map<String,Object> A_map = new HashMap<>();
        Map<String,Object> B_map = new HashMap<>();
        Map<String,Object> bar_map = new HashMap<>();
        for (int i = 0; i < A_x.get(count).split(",").length; i=i+2) {
            Map<String,String> map = new HashMap<>();
            map.put("x",A_x.get(count).split(",")[i].substring(0,A_x.get(count).split(",")[i].indexOf(".")+4));
            map.put("y",A_y.get(count).split(",")[i]);
            A_line.add(map);
        }
        A_point.put("x",A_x.get(count).split(",")[A_x.get(count).split(",").length-1]);
        A_point.put("point",A_y.get(count).split(",")[A_x.get(count).split(",").length-1]);
        A_map.put("line",A_line);
        A_map.put("point",A_point);
        //第二条线
        List<Map<String,String>> B_line1 = new ArrayList<>();
        List<Map<String,String>> B_line2 = new ArrayList<>();
        Map<String,String> B_point = new HashMap<>();
        List<String> B_x = fileClient.getContent("/jar/db/2-x.txt");
        List<String> B_y1 = fileClient.getContent("/jar/db/2-y1.txt");
        List<String> B_y2 = fileClient.getContent("/jar/db/2-y2.txt");
        for (int i = 0; i < B_y1.get(count).split(",").length; i=i+2) {
            Map<String,String> map = new HashMap<>();
            map.put("x",B_x.get(count).split(",")[i].substring(0,B_x.get(count).split(",")[i].indexOf(".")+4));
            map.put("y",B_y1.get(count).split(",")[i]);
            B_line1.add(map);
        }
        for (int i = 0; i < B_y2.get(count).split(",").length; i=i+2) {
            Map<String,String> map = new HashMap<>();
            map.put("x",B_x.get(count).split(",")[i].substring(0,B_x.get(count).split(",")[i].indexOf(".")+4));
            map.put("y",B_y2.get(count).split(",")[i]);
            B_line2.add(map);
        }
        B_point.put("x",B_x.get(count).split(",")[B_x.get(count).split(",").length-1]);
        B_point.put("y","0");
        B_map.put("line1",B_line1);
        B_map.put("line2",B_line2);
        B_map.put("point",B_point);
        //柱子
        List<Map<String,String>> bar_list = new ArrayList<>();
        List<String> content = fileClient.getContent("/jar/db/bar_1.txt");
        for (int i = 0; i <5 ; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("name",content.get(i).split(",")[1]);
            map.put("value",content.get(i).split(",")[2]);
            bar_list.add(map);
        }
        data_map.put("line1",A_map);
        data_map.put("line2",B_map);
        data_map.put("bar",bar_list);
        jtSocket.sendMessage(JSON.toJSONString(data_map));
        count = count+1;
        if(count==5){
            count = 0 ;
        }
    }
}
