package iscas.stategrid.mapdata.service.impl;

import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.mapper.VoiceDao;
import iscas.stategrid.mapdata.service.KongJService;
import iscas.stategrid.mapdata.service.VoiceService;
import iscas.stategrid.mapdata.mapper.LocationMapper;
import iscas.stategrid.mapdata.util.StaticResource;
import iscas.stategrid.mapdata.websocket.MapTopoWebSocket;
import iscas.stategrid.mapdata.websocket.VoiceSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yangshitong
 * @Date 2019/12/12 17:13
 * @Version 1.0
 * @Description:
 */
@Service
public class VoiceServiceImpl implements VoiceService {

    @Autowired
    private VoiceSocket voiceSocket;
    @Autowired
    private VoiceDao voiceDao;
    @Autowired
    private MapTopoWebSocket mapTopoWebSocket;
    @Autowired
    private LocationMapper stLocationEntityMapper;
    @Autowired
    private KongJService jcInfoService;
    private List<Map<String,String>> info;
    private boolean isControl = false;
    @Override
    public String queryCommand(String commandType, String area) {
        String message = "查询成功";
        Map<String, String> voice_map = new HashMap<>();
        String type = "2";
        String msg = "";
        String url = "http://192.168.101.80:8100/tts?access_token=speech&language=zh&domain=1&voice_name=静静&text=";
        if (commandType.equals("01")) {
            String index = jcInfoService.getIndexByFlag("1");
            msg = url + "当前电网安全性指标是" + index;
        } else if (commandType.equals("02")) {
            String index = jcInfoService.getIndexByFlag("2");
            msg = url + "当前电网经济性指标是" + index;
        } else if (commandType.equals("03")) {
            String index = jcInfoService.getIndexByFlag("3");
            msg = url + "当前电网综合评估指标是" + index;
        } else if (commandType.equals("04")) {
            String index = jcInfoService.getIndexByFlag("4");
            msg = url + "当前电网静态安全指标是" + index;
        } else if (commandType.equals("05")) {
            String index = jcInfoService.getIndexByFlag("5");
            msg = url + "当前电网暂态安全指标是" + index;
        } else if (commandType.equals("06")) {
            String police = jcInfoService.getPoliceInfo();
            msg = url + "当前电网的调控策略有：" + police;
        } else if (commandType.equals("07")) {

        } else if (commandType.equals("08")) {
            String percent = jcInfoService.getZNByArea(area);
            if (percent.equals("")) {
                message = "查询失败";
                msg = url + "没有查到相关信息";
            } else {
                msg = url + area + "地区的阻尼比是" + percent;
                message = "查询成功";
            }
        }
        voice_map.put("type", type);
        voice_map.put("message", msg);
        voiceSocket.sendMessage(JSON.toJSONString(voice_map));
        return message;
    }

    @Override
    public String operate(String commandType, String parameter) {
        String url = "http://192.168.101.80:8100/tts?access_token=speech&language=zh&domain=1&voice_name=静静&text=";
        String message = "fail";
        Map<String, Object> voice_map = new HashMap<>();
        if ("01".equals(commandType)){
            String level="";
            if("全国".equals(parameter)){
                level="全国";
            }
            else if(StaticResource.PROVINCE_Set.contains(parameter)){
                level="省级";
                Map<String,Object> map=stLocationEntityMapper.selectPinyinOfProvince(parameter);
                voice_map.put("pinyin",map.get("english"));
            }
            else if(StaticResource.AREA_Set.contains(parameter)){
                level="区域";
            }
            else if(StaticResource.CITY_SET.contains(parameter)){
                level="市级";
            }
            voice_map.put("type","1");
            voice_map.put("level",level);
            voice_map.put("area",parameter);
            voice_map.put("voice",url+"已为您切换到"+parameter);
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            message = "success";
        }
        else if("02".equals(commandType)){
            voice_map.put("type","1");
            voice_map.put("area","返回上一级");
            voice_map.put("level","");
            voice_map.put("voice",url+"返回成功");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            message = "success";
        }else if("03".equals(commandType)){
            voice_map.put("type","3");
            voice_map.put("name",parameter);
            voice_map.put("voice",url+"已为您切换到"+parameter+"界面");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            message = "success";
        }else if("04".equals(commandType)){
            voice_map.put("type","3");
            voice_map.put("name",parameter);
            voice_map.put("voice",url+"已为您切换到"+parameter+"场景");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            message = "success";
        }else if("05".equals(commandType)){
            Map<String,Object> message_map = new HashMap<>();
            Map<String,String> error_point = new HashMap<>();
            List<Map<String,String>> error_line = new ArrayList<>();
            List<Map<String,String>> from_list = voiceDao.getLineInfo(voiceDao.getErrorInfo(parameter).get("from"));
            List<Map<String,String>> to_list =   voiceDao.getLineInfo(voiceDao.getErrorInfo(parameter).get("to"));
            for (int i = 0; i <from_list.size(); i++) {
                Map<String,String> line_map = new HashMap<>();
                line_map.put("Flng",voiceDao.getLocationByName(from_list.get(i).get("from")).get("lng"));
                line_map.put("Flat",voiceDao.getLocationByName(from_list.get(i).get("from")).get("lat"));
                line_map.put("Tlng",voiceDao.getLocationByName(from_list.get(i).get("to")).get("lng"));
                line_map.put("Tlat",voiceDao.getLocationByName(from_list.get(i).get("to")).get("lat"));
                error_line.add(line_map);
            }
            for (int i = 0; i <to_list.size(); i++) {
                Map<String,String> line_map = new HashMap<>();
                line_map.put("Flng",voiceDao.getLocationByName(to_list.get(i).get("from")).get("lng"));
                line_map.put("Flat",voiceDao.getLocationByName(to_list.get(i).get("from")).get("lat"));
                line_map.put("Tlng",voiceDao.getLocationByName(to_list.get(i).get("to")).get("lng"));
                line_map.put("Tlat",voiceDao.getLocationByName(to_list.get(i).get("to")).get("lat"));
                error_line.add(line_map);
            }
            message_map.put("area","华中");
            message_map.put("JZStatus","2");
            message_map.put("vlevel","");
            error_point.put("Flng",voiceDao.getErrorInfo(parameter).get("Flng"));
            error_point.put("Flat",voiceDao.getErrorInfo(parameter).get("Flat"));
            error_point.put("Tlng",voiceDao.getErrorInfo(parameter).get("Tlng"));
            error_point.put("Tlat",voiceDao.getErrorInfo(parameter).get("Tlat"));
            error_point.put("percent",voiceDao.getErrorInfo(parameter).get("percent"));
            message_map.put("error_point",error_point);
            message_map.put("error_line",error_line);
            String message_json = JSON.toJSONString(message_map);
            mapTopoWebSocket.onMessage(message_json);
            voice_map.put("type","2");
            voice_map.put("voice",url+"第"+parameter+"类故障设置成功");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            try {
                Thread.sleep(1500);
                voice_map.put("type","2");
                voice_map.put("voice",url+"华中地区在此故障下稳定的概率是");
                voiceSocket.sendMessage(JSON.toJSONString(voice_map));
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map<String,String> ZC_map = new HashMap<>();
            ZC_map.put("area","华中");
            ZC_map.put("JZStatus","1");
            ZC_map.put("vlevel","");
            mapTopoWebSocket.onMessage(JSON.toJSONString(ZC_map));
            message = "success";
        }else if("06".equals(commandType)){
            voice_map.put("type","2");
            //传输调控后裕度信息
            isControl = true;
            voice_map.put("voice",url+"调控策略已注入");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
        }else if("0A".equals(commandType)){
            voice_map.put("type","3");
            voice_map.put("name","模型测辨");
            voice_map.put("voice",url+"已为您切换到模型测辨界面");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
        }else if("0B".equals(commandType)){
            voice_map.put("type","3");
            voice_map.put("name","平台监控");
            voice_map.put("voice",url+"已为您切换到大数据平台监控界面");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
        }
        return message;
    }
    @Override
    public boolean getControl(){
        return isControl;
    }
    @Override
    public List<Map<String,String>> getInfo(){
        return info;
    }
}
