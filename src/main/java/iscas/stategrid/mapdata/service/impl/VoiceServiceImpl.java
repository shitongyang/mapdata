package iscas.stategrid.mapdata.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import iscas.stategrid.mapdata.service.KongJService;
import iscas.stategrid.mapdata.service.VoiceService;
import iscas.stategrid.mapdata.mapper.st_locationEntityMapper;
import iscas.stategrid.mapdata.util.StaticResource;
import iscas.stategrid.mapdata.websocket.MapTopoWebSocket;
import iscas.stategrid.mapdata.websocket.VoiceSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private MapTopoWebSocket mapTopoWebSocket;
    @Autowired
    private st_locationEntityMapper stLocationEntityMapper;
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
            String hz = jcInfoService.getHZByArea(area);
            if (hz.equals("")) {
                message = "查询失败";
                msg = url + "没有查到相关信息";
            } else {
                msg = url + area + "地区的振荡频率是" + hz;
            }
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
            message = "success";
        }
        else if("02".equals(commandType)){
            voice_map.put("type","1");
            voice_map.put("area","返回上一级");
            voice_map.put("level","");
            voice_map.put("voice",url+"返回成功");
            message = "success";
        }else if("03".equals(commandType)){
            voice_map.put("type","3");
            voice_map.put("name",parameter);
            voice_map.put("voice",url+"已为您切换到"+parameter+"界面");
        }else if("04".equals(commandType)){
            voice_map.put("type","3");
            voice_map.put("name",parameter);
            voice_map.put("voice",url+"已为您切换到"+parameter+"场景");
        }else if("05".equals(commandType)){
            Map<String,String> message_map = new HashMap<>();
            message_map.put("area","华中");
            message_map.put("JZStatus","2");
            message_map.put("vlevel","");
            String message_json = JSON.toJSONString(message_map);
            mapTopoWebSocket.sendMessage(message_json);
            voice_map.put("type","2");
            //传输故障信息
            Map<String,String> error_map = new HashMap<>();
            error_map.put("from","");
            error_map.put("to","");
            error_map.put("message","");
            voice_map.put("voice",url+"故障设置成功");
        }else if("06".equals(commandType)){
            voice_map.put("type","2");
            //传输调控后裕度信息
            isControl = true;
            voice_map.put("voice",url+"调控策略已注入");
        }else if("0A".equals(commandType)){
            voice_map.put("type","3");
            voice_map.put("name","模型测辨");
            voice_map.put("voice",url+"已为您切换到模型测辨界面");
        }else if("0B".equals(commandType)){
            voice_map.put("type","3");
            voice_map.put("name","平台监控");
            voice_map.put("voice",url+"已为您切换到大数据平台监控界面");
        }
        voiceSocket.sendMessage(JSON.toJSONString(voice_map));
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
