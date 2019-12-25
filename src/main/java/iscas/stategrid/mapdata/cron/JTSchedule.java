package iscas.stategrid.mapdata.cron;

import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.util.FileClient;
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
        FileClient fileClient = new FileClient();
        List<String> A_x = fileClient.getContent("/jar/db/1-x.txt");
        List<String> A_y = fileClient.getContent("/jar/db/1-y.txt");
        List<Map<String,String>> A_line = new ArrayList<>();
        Map<String,String> A_point = new HashMap<>();
        Map<String,Object> A_map = new HashMap<>();
        Map<String,String> B_map = new HashMap<>();
        for (int i = 0; i < A_x.get(count).split(",").length; i=i+2) {
            Map<String,String> map = new HashMap<>();
            map.put("x",A_x.get(count).split(",")[i]);
            map.put("y",A_y.get(count).split(",")[i]);
            A_line.add(map);
        }
        A_point.put("x",A_x.get(count).split(",")[A_x.get(count).split(",").length]);
        A_point.put("point",A_y.get(count).split(",")[A_x.get(count).split(",").length]);
        A_map.put("line",A_line);
        A_map.put("point",A_point);
        //第二条线
//        List<String> B_x = fileClient.getContent("/jar/db/2-x.txt");
//        List<String> B_y1 = fileClient.getContent("/jar/db/2-y1.txt");
//        List<String> B_y2 = fileClient.getContent("/jar/db/2-y2.txt");
        jtSocket.sendMessage(JSON.toJSONString(A_map));
        count = count+1;
        if(count==5){
            count = 0 ;
        }
    }
}
