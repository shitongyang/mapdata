package iscas.stategrid.mapdata.cron;


import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.mapper.WinPositionDao;
import iscas.stategrid.mapdata.websocket.ModelSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.*;

/**
 * @author : lvxianjin
 * @Date: 2019/12/29 21:05
 * @Description:
 */
@Component
@EnableScheduling
public class ModelSchedule implements SchedulingConfigurer {
    @Autowired
    private ModelSocket modelSocket;
    @Autowired
    private WinPositionDao winPositionDao;
    @Scheduled(fixedRate=1000)
    public void doThing(){
        List<Map<String,String>> info = new ArrayList<>();
        List<String> WindName = winPositionDao.getWindName();
        for (int i = 0; i <WindName.size(); i++) {
            Map<String,String> map = new HashMap<>();
            map.put("name",WindName.get(i));
            int w = (int) (Math.random() * (20000 - 500) + 500);
            double p = (int) (Math.random() * (800 - 200) + 200)*0.1;
            double q = (int) (Math.random() * (200 - 10) + 10)*0.01;
            NumberFormat Nformat = NumberFormat.getInstance();
            // 设置小数位数。
            Nformat.setMaximumFractionDigits(2);
            if(i%2==0){
                map.put("W",String.valueOf(295070-w));
                map.put("P",Nformat.format(366.101-p));
                map.put("Q","-"+Nformat.format(5.86096-q));
            }else {
                map.put("W",String.valueOf(295070+w));
                map.put("P",Nformat.format(366.101+p));
                map.put("Q","-"+Nformat.format(5.86096+q));
            }
            info.add(map);
        }
        modelSocket.sendMessage(JSON.toJSONString(info));
    }
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {

    }
}
