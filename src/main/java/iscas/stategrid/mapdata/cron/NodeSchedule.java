package iscas.stategrid.mapdata.cron;


import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.mapper.LocationMapper;
import iscas.stategrid.mapdata.service.MapService;
import iscas.stategrid.mapdata.util.RedisClient;
import iscas.stategrid.mapdata.websocket.MapTopoWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 节点的定时任务类
 *
 * @author : lvxianjin
 * @Date: 2019/10/22 19:45
 * @Description:
 */
@Component
@EnableScheduling
public class NodeSchedule implements SchedulingConfigurer {
    @Autowired
    private MapService Service;
    @Autowired
    private MapTopoWebSocket Socket;
    @Autowired
    private LocationMapper Dao;
    private static final String DEFAULT_CRON = "0/5 * * * * ?";
    private String cron = DEFAULT_CRON;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                RedisClient client = new RedisClient();
                String message = client.getValue("id");
                String array[]=message.split(",");
                String isStatic="";
                if(array.length>=2){
                    isStatic=array[1];
                    System.out.println(isStatic+"............");
                    if(array[1].equals("2")) {
                        setCron("0/1 * * * * ?");
                    }
                    if(array[1].equals("1")){
                        setCron("0/5 * * * * ?");
                    }
                }
                else {
                    setCron("0/1 * * * * ?");
                }


                List<Map<String,Object>> data1 = Service.getTopoLocation(message);

                List<Map<String,Object>> data3 = Service.getTopoLine(message);
                Map<String,Object> data_map=Socket.resultMap(data3,data1,message);
                if(array[0].equals("全国")&&isStatic.equals("2")){
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
                    data_map.put("badPoint",badPointList);
                    data_map.put("badLine",badLineList);
                }
                Socket.sendMessage(JSON.toJSONString(data_map));

            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                //CronTrigger trigger = new CronTrigger(cron);
               // Date nextExecDate = trigger.nextExecutionTime(triggerContext);
               // return nextExecDate;
                return null;
            }
        });
    }
    public void setCron(String cron) {
        this.cron = cron;
    }
    public static double add(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(String.valueOf(m1));
        BigDecimal p2 = new BigDecimal(String.valueOf(m2));
        return p1.add(p2).doubleValue();
    }
}
