package iscas.stategrid.mapdata.cron;

import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.service.WinPositionService;
import iscas.stategrid.mapdata.websocket.WinPositionSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 风电场的定时任务类
 *
 * @author : lvxianjin
 * @Date: 2019/10/22 19:45
 * @Description:
 */

@Component
@EnableScheduling
public class WindSchedule {
        //implements SchedulingConfigurer {
    @Autowired
    private WinPositionService winPositionService;
    @Autowired
    private WinPositionSocket winPositionSocket;
    private static final String DEFAULT_CRON = "0/2 * * * * ?";
    private String cron = DEFAULT_CRON;
   /* @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                List<Map<String,String>> DYW_info = winPositionService.getDYWInfo();
                winPositionSocket.sendMessage(JSON.toJSONString(DYW_info));
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
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
    }*/



   @Scheduled(fixedRate=200)
   public void doThing(){
       List<Map<String,String>> DYW_info = winPositionService.getDYWInfo();
       winPositionSocket.sendMessage(JSON.toJSONString(DYW_info));
   }

}
