package iscas.stategrid.mapdata.service.impl;

import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.mapper.VoiceDao;
import iscas.stategrid.mapdata.service.KongJService;
import iscas.stategrid.mapdata.service.VoiceService;
import iscas.stategrid.mapdata.mapper.LocationMapper;
import iscas.stategrid.mapdata.Utils.StaticResource;
import iscas.stategrid.mapdata.service.WinPositionService;
import iscas.stategrid.mapdata.websocket.ControlSocket;
import iscas.stategrid.mapdata.websocket.MapTopoWebSocket;
import iscas.stategrid.mapdata.websocket.VoiceSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;
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
    private KongJService kongJService;
    @Autowired
    private WinPositionService winPositionService;
    private boolean isControl = false;
    @Override
    public String queryCommand(String commandType, String area) {
        String message = "查询失败";
        Map<String, String> voice_map = new HashMap<>();
        String type = "2";
        String msg = "";
        String url = "http://192.168.101.80:8100/tts?access_token=speech&language=zh&domain=1&voice_name=静静&text=";
        if (commandType.equals("01")) {
            String index = kongJService.getIndexByFlag("1");
            message = "查询成功";
            msg = url + "当前电网安全性指标是" + index;
        } else if (commandType.equals("02")) {
            String index = kongJService.getIndexByFlag("2");
            msg = url + "当前电网经济性指标是" + index;
            message = "查询成功";
        } else if (commandType.equals("03")) {
            String index = kongJService.getIndexByFlag("3");
            msg = url + "当前电网综合评估指标是" + index;
            message = "查询成功";
        } else if (commandType.equals("04")) {
            String index = kongJService.getIndexByFlag("4");
            msg = url + "当前电网静态安全指标是" + index;
            message = "查询成功";
        } else if (commandType.equals("05")) {
            String index = kongJService.getIndexByFlag("5");
            msg = url + "当前电网暂态安全指标是" + index;
            message = "查询成功";
        } else if (commandType.equals("06")) {

        } else if (commandType.equals("07")) {
            String hz = kongJService.getModelByArea(area,"hz");
            if (hz.equals("")) {
                msg = url + "没有查到相关信息";
            } else {
                msg = url + area + "地区的振荡频率是" + hz;
                message = "查询成功";
            }
        } else if (commandType.equals("08")) {
            String percent = kongJService.getModelByArea(area,"percent");
            if (percent.equals("")) {
                msg = url + "没有查到相关信息";
            } else {
                msg = url + area + "地区的阻尼比是" + percent;
                message = "查询成功";
            }
        }
        voice_map.put("type", type);
        voice_map.put("voice", msg);
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
            List<Map<String,String>> bj_line = new ArrayList<>();
            List<Map<String,String>> bj_point = new ArrayList<>();
            String start_station = voiceDao.getErrorInfo(parameter).get("from");
            String end_station = voiceDao.getErrorInfo(parameter).get("to");
            List<Map<String,String>> from_list = voiceDao.getLineInfo(start_station);
            List<Map<String,String>> to_list =   voiceDao.getLineInfo(end_station);
            for (int i = 0; i <from_list.size(); i++) {
                Map<String,String> line_map = new HashMap<>();
                line_map.put("Flng",voiceDao.getLocationByName(from_list.get(i).get("from")).get("lng"));
                line_map.put("Flat",voiceDao.getLocationByName(from_list.get(i).get("from")).get("lat"));
                line_map.put("Tlng",voiceDao.getLocationByName(from_list.get(i).get("to")).get("lng"));
                line_map.put("Tlat",voiceDao.getLocationByName(from_list.get(i).get("to")).get("lat"));
                if(from_list.get(i).get("from").equals(start_station)&&from_list.get(i).get("to").equals(end_station)){
                    if(error_line.size()<1){
                        error_line.add(line_map);
                    }
                }else {
                    bj_line.add(line_map);
                }
            }
            for (int i = 0; i <to_list.size(); i++) {
                Map<String,String> line_map = new HashMap<>();
                line_map.put("Flng",voiceDao.getLocationByName(to_list.get(i).get("from")).get("lng"));
                line_map.put("Flat",voiceDao.getLocationByName(to_list.get(i).get("from")).get("lat"));
                line_map.put("Tlng",voiceDao.getLocationByName(to_list.get(i).get("to")).get("lng"));
                line_map.put("Tlat",voiceDao.getLocationByName(to_list.get(i).get("to")).get("lat"));
                if(to_list.get(i).get("from").equals(start_station)&&to_list.get(i).get("to").equals(end_station)){
                }else {
                    bj_line.add(line_map);
                }
            }
            for (int i = 0; i < bj_line.size(); i++) {
                Map<String,String> map = new HashMap<>();
                map.put("lng",bj_line.get(i).get("Flng"));
                map.put("lat",bj_line.get(i).get("Flat"));
                if(!map.equals(voiceDao.getLocationByName(start_station))&&!map.equals(voiceDao.getLocationByName(end_station))){
                    bj_point.add(map);
                }
            }
            for (int i = 0; i < bj_line.size(); i++) {
                Map<String,String> map = new HashMap<>();
                map.put("lng",bj_line.get(i).get("Tlng"));
                map.put("lat",bj_line.get(i).get("Tlat"));
                if(!map.equals(voiceDao.getLocationByName(start_station))&&!map.equals(voiceDao.getLocationByName(end_station))){
                    bj_point.add(map);
                }
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
            message_map.put("bj_line",bj_line);
            message_map.put("bj_point",bj_point);
            String message_json = JSON.toJSONString(message_map);
            mapTopoWebSocket.onMessage(message_json);
            voice_map.put("type","2");
            voice_map.put("voice",url+"第"+parameter+"类故障设置成功");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            String rate = "";
            try {
                Thread.sleep(1500);
                voice_map.put("type","2");
                if("1".equals(parameter)){
                    rate =  "0.56";
                }else if("2".equals(parameter)){
                    rate =  "0.43";
                }else {
                    rate =  "0.29";
                }
                ControlSocket.value=String.valueOf(Double.parseDouble(rate)*0.01);
                //ControlSocket.setValue(String.valueOf(Double.parseDouble(rate)*0.01));
                voice_map.put("voice",url+"华中地区在此故障下稳定的概率是百分之"+rate);
                voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            message = "success";
        }else if("06".equals(commandType)){
            voice_map.put("type","2");
            voice_map.put("voice",url+"正在调控");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map<String,Object> message_map = new HashMap<>();
            List<Map<String,String>> list = voiceDao.getWeak("全国");
            List<Map<String,String>> weak_Location = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String,String> map = list.get(i);
                map.put("height",String.valueOf((int) (Math.random() * (15 - 8) + 8)));
                map.put("color","green");
                weak_Location.add(map);
            }
            message_map.put("area","全国");
            message_map.put("JZStatus","3");
            message_map.put("vlevel","");
            message_map.put("weak_Location",weak_Location);
            mapTopoWebSocket.onMessage(JSON.toJSONString(message_map));
            voice_map.put("type","2");
            voice_map.put("voice",url+"调控策略已生效，薄弱点裕度有明显提高");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            message = "success";
        }else if("07".equals(commandType)){
            /*
            * 进入哈尔滨风电机组
            * */
            voice_map.put("type","4");
            voice_map.put("voice",url+"已为您展示到哈尔滨风电机组");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            message = "success";
        }else if("08".equals(commandType)){
            //xx站的电压是多少
            Map<String,String> map = voiceDao.getLocationByName("%"+parameter);
            int type = Integer.parseInt(map.get("type").substring(0,map.get("type").indexOf("k")));
            double d = ((int) (Math.random() * (10 - 1) + 1)) * 0.01;
            map.put("value",String.valueOf(type-(type*0.05)*d)+"kv");
            voice_map.put("type","5");
            voice_map.put("data",JSON.toJSONString(map));
            voice_map.put("voice",url+parameter+"的电压是"+String.valueOf(map.get("value")));
            message = "success";
        }else if("09".equals(commandType)){
            //展示薄弱节点
            Map<String,Object> message_map = new HashMap<>();
            List<Map<String,String>> list = voiceDao.getWeak("全国");
            List<Map<String,String>> china_tpLocation = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String,String> map = list.get(i);
                map.put("height",String.valueOf((int) (Math.random() * (7 - 3) + 3)));
                map.put("color","red");
                china_tpLocation.add(map);
            }
            message_map.put("area","全国");
            message_map.put("JZStatus","3");
            message_map.put("vlevel","");
            message_map.put("weak_Location",china_tpLocation);
            mapTopoWebSocket.onMessage(JSON.toJSONString(message_map));
            voice_map.put("type","2");
            voice_map.put("voice",url+"已为您展示薄弱节点信息");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            message = "success";
        }else if("0A".equals(commandType)){
            voice_map.put("type","3");
            voice_map.put("name","模型测辨");
            voice_map.put("voice",url+"已为您切换到模型测辨界面");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            message = "success";
        }else if("0B".equals(commandType)){
            voice_map.put("type","3");
            voice_map.put("name","平台监控");
            voice_map.put("voice",url+"已为您切换到大数据平台监控界面");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            message = "success";
        }else if("0C".equals(commandType)){
            //切换到综合分析界面
        }else if("0D".equals(commandType)){
            //展示潮流数据
            Map<String,Object> message_map = new HashMap<>();
            message_map.put("area","全国");
            message_map.put("JZStatus","1");
            message_map.put("vlevel","");
            mapTopoWebSocket.onMessage(JSON.toJSONString(message_map));
            voice_map.put("type","2");
            voice_map.put("voice",url+"已为您展示潮流数据");
            voiceSocket.sendMessage(JSON.toJSONString(voice_map));
            message = "success";
        }else if("0E".equals(commandType)){
            //降低风速
            winPositionService.setTime("1");
            voice_map.put("type","5");
            voice_map.put("voice",url+"风速已降低");
            message = "success";
        }else if("0F".equals(commandType)){
            //提高风速
            winPositionService.setTime("0");
            voice_map.put("type","5");
            voice_map.put("voice",url+"风速已提高");
            message = "success";
        }
        return message;
    }
    @Override
    public boolean getControl(){
        return isControl;
    }
}
