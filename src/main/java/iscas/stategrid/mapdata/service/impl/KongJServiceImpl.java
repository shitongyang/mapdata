package iscas.stategrid.mapdata.service.impl;
import com.alibaba.fastjson.JSONObject;
import iscas.stategrid.mapdata.Utils.RedisClient;
import iscas.stategrid.mapdata.mapper.KongJianMapper;
import iscas.stategrid.mapdata.service.KongJService;
import iscas.stategrid.mapdata.mapper.LocationMapper;
import iscas.stategrid.mapdata.Utils.FileClient;
import iscas.stategrid.mapdata.Utils.StaticResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static iscas.stategrid.mapdata.Utils.StaticResource.errorResult;

/**
 * @author : lvxianjin
 * @Date: 2019/10/23 15:59
 * @Description: 控件信息
 */
@Service
public class KongJServiceImpl implements KongJService {

    NumberFormat Nformat = NumberFormat.getInstance();
    private int count = 1;
    /*
     * 安全性指标
     * */
    private String index_1 = "0";
    /*
     * 经济型指标
     * */
    private String index_2 = "0";
    /*
     * 综合指标
     * */
    private String index_3 = "0";
    /*
     * 静态安全分析指标
     * */
    private String index_4 = "0";
    /*
     * 暂态安全分析指标
     * */
    private String index_5 = "0";
    /*
    * 薄弱节点id
    * */
    List<Integer> ids = new ArrayList<>();
    /*
    * 六大地区振荡频率和阻尼比信息
     */
    List<Map<String, String>> area_info = new ArrayList<>();

    @Autowired
    private LocationMapper stLocationEntityMapper;

    @Autowired
    private KongJianMapper kongJianMapper;

    @Override
    public List<String> getErrorInfo() {
        List<String> info = new ArrayList<>();
        ids = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int node_id = (int) (Math.random() * (39 - 1) + 1);
            ids.add(node_id);
            String name = stLocationEntityMapper.getNameById(String.valueOf(node_id));
            info.add(name);
        }
        return info;
    }
    @Override
    public Map<String, String> getIndex() {
        Map<String, String> info = new HashMap<>();
        RedisClient redisClient = new RedisClient();
        index_1 = redisClient.getValue("index1");
        index_2 = redisClient.getValue("index2");
        index_3 = redisClient.getValue("index3");
        index_4 = redisClient.getValue("index4");
        index_5 = redisClient.getValue("index0");
        info.put("index_1", index_1);
        info.put("index_2", index_2);
        info.put("index_3", index_3);
        info.put("index_4", index_4);
        info.put("index_5", index_5);
        if(info.get("index_1") == null){
            System.out.println("仪表盘发生错误");
        }
        return info;
    }
    @Override
    public String getIndexByFlag(String flag) {
        String index = "";
        if(flag.equals("1")){
            index = "百分之"+index_1;
        }else if(flag.equals("2")){
            index = "百分之"+index_2;
        }else if(flag.equals("3")){
            index = "百分之"+index_3;
        }else if(flag.equals("4")){
            index = "百分之"+index_4;
        }else {
            index = "百分之"+index_5;
        }
        return index;
    }

    @Override
    public Map<String,Object> getAreaInfo() {
        Map<String,Object> resultMap = new HashMap<>();
        for(String s:StaticResource.AREA_Set){
            resultMap.put(s,getAreaZDandZN(s));
        }
        return resultMap;
    }

    @Override
    public String getModelByArea(String area,String type) {
        String rootPath = "/jar/lkb/";
        FileClient fileClient = new FileClient();
        String flag = "";
            if ("华北".equals(area)) {
                flag = "0/";
            } else if ("华东".equals(area)) {
                flag = "1/";
            } else if ("华中".equals(area)) {
                flag = "2/";
            } else if ("东北".equals(area)) {
                flag = "3/";
            } else if ("西北".equals(area)) {
                flag = "4/";
            } else if ("西南".equals(area)) {
                flag = "5/";
            }
            String filePath = rootPath+flag+String.valueOf(2)+".txt";
            List<String> content = fileClient.getContent(filePath);
            String str_hz = content.get(1).split(",")[1];
            String str_percent = content.get(1).split(",")[0];
            double percent = Double.parseDouble(str_percent)*100;
            double hz = Double.parseDouble(str_hz);
            NumberFormat Nformat = NumberFormat.getInstance();
            // 设置小数位数。
            Nformat.setMaximumFractionDigits(2);
            if("hz".equals(type)){
                return Nformat.format(hz);
            }else {
                return Nformat.format(percent)+"%";
            }
        }

    @Override
    public List<Map<String, String>>  getImIndex(String area,String isStatic) {
        //获取稳定指标
        List<Map<String,String>> list = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        if(area.equals("全国")){
            ids.add("江西.金坑站");
            ids.add("吉林.荣家变");
            ids.add("山东.仲连站");
            ids.add("江西.车溪站");
        }
        else if(StaticResource.AREA_Set.contains(area)){
           ids=stLocationEntityMapper.selectProvinceByArea(area);
        }
        else if(StaticResource.PROVINCE_Set.contains(area)){
            ids=stLocationEntityMapper.selectCityByProvince(area);
        }
        else if(StaticResource.CITY_SET.contains(area)){
            ids=stLocationEntityMapper.selectCountyByCity(area);
        }
        NumberFormat Nformat = NumberFormat.getInstance();
        // 设置小数位数
        Nformat.setMaximumFractionDigits(2);
        int length=ids.size();
        for (int i = 0; i <length ; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("nodeName",ids.get(i));
            double d = ((int) (Math.random() * (60 - 40) + 40)) * 0.01;
            if("2".equals(isStatic)){
                d=((int) (Math.random() * (60 - 40) + 20)) * 0.01;
            }
            map.put("index",Nformat.format(d));
            list.add(map);
        }
        return list;
    }



    public List<Map<String, Object>> getControlPolice() {
        //获取设备调控策略
        //对应的是device_police表
        List<Map<String,Object>> list = kongJianMapper.getDevicePolice();
        List<Map<String,Object>> resultList =new ArrayList<>();
        Nformat.setMaximumFractionDigits(2);
        int length=list.size();
        for (int i = 0; i <length ; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("deviceName",list.get(i).get("nodeName").toString()+list.get(i).get("policeName"));
            map.put("index",list.get(i).get("index"));
            resultList.add(map);
        }
        return resultList;
    }
    @Override
    public List<Map<String, String>> getDeviceMotaiInfo(String area, String modelName) {
        //获取设备模态信息
        String rootPath = "/jar/lkb/";
        String flag = "";
        FileClient fileClient = new FileClient();
        if ("华北".equals(area)) {
            flag = "0/";
        } else if ("华东".equals(area)){
            flag = "1/";
        } else if ("华中".equals(area)) {
            flag = "2/";
        } else if ("东北".equals(area)) {
            flag = "3/";
        } else if ("西北".equals(area)) {
            flag = "4/";
        } else if ("西南".equals(area)) {
            flag = "5/";
        }else {
            flag = "2/";
        }
        List<Map<String,String>> list = new ArrayList<>();
        List<String> content = fileClient.getContent(rootPath+flag+String.valueOf(count)+".txt");
        int row_count = Integer.parseInt(modelName.substring(2));
        String dev_value[] = content.get(row_count).split(",");
        for (int i = 2; i <dev_value.length ; i++) {
            if(!"0".equals(dev_value[i])){
                Map<String,String> map = new HashMap<>();
                map.put("deviceName",content.get(0).split(",")[i].split("-")[1]);
                map.put("value",dev_value[i]);
                if(list.size()<7){
                    list.add(map);
                }
            }
        }
        return list;
    }

    @Override
    public List<Map<String,Object>> getAreaZDandZN(String area)
    {
        //获得区域的震荡频率和阻尼比
        String rootPath = "/jar/lkb/";
        String flag = "";
        FileClient fileClient = new FileClient();
        if ("华北".equals(area)) {
            flag = "0/";
        } else if ("华东".equals(area)){
            flag = "1/";
        } else if ("华中".equals(area)) {
            flag = "2/";
        } else if ("东北".equals(area)) {
            flag = "3/";
        } else if ("西北".equals(area)) {
            flag = "4/";
        } else if ("西南".equals(area)) {
            flag = "5/";
        }else {
            flag = "2/";
        }
        List<Map<String,Object>> list = new ArrayList<>();
        List<String> content = fileClient.getContent(rootPath+flag+String.valueOf(count)+".txt");
        for (int i = 1; i < content.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            String str_hz = content.get(i).split(",")[1];
            String str_percent = content.get(i).split(",")[0];
            double percent = Double.parseDouble(str_percent)*100;
            double hz = Double.parseDouble(str_hz);
            NumberFormat Nformat = NumberFormat.getInstance();
            // 设置小数位数。
            Nformat.setMaximumFractionDigits(2);
            map.put("modelName","模式"+String.valueOf(i));
            map.put("hz",Nformat.format(hz));
            map.put("percent",Nformat.format(percent)+"%");
            list.add(map);
        }

        count = count+1;
        if(count>3){
            count=1;
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getGuZhangBaoJing(String para, String status) {
        List<Map<String, Object>> list= kongJianMapper.getBaoJing(para,status);
        List <Map<String, Object>> resultList=new ArrayList<>();
        int length=(list==null?0:list.size());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for(int i=0;i<length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("message",df.format(new Date())+" "+list.get(i).get("info"));
            map.put("type", list.get(i).get("level"));
            String level=list.get(i).get("level").toString();
            map.put("dLevel",StaticResource.levelMap.get(level));
            resultList.add(map);
        }
        return resultList;
    }

    public List<Map<String,Object>> getWeather(List<String> list){
        List<Map<String,Object>> resultList = new ArrayList<>();
        Nformat.setMaximumFractionDigits(2);
        int length=list.size();
        for (int i = 0; i <length ; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",list.get(i));
            map.put("temperature",Nformat.format(Math.random()*15+10));
            map.put("humidity",Nformat.format(Math.random()*15+1));
            map.put("weather","晴");
            if(i%3==0){
                map.put("weather","阴");
            }
            resultList.add(map);
        }
        return resultList;
    }

    public List<Map<String, Object>> getBaoJing(String area){
        //获取报警信息
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String,Object>> list1 = kongJianMapper.getBoRuo(area);
        List<Map<String,Object>> list = new ArrayList<>();
        for(int i=0;i<list1.size();i++){
            double police = Double.parseDouble(list1.get(i).get("before").toString());
            Nformat.setMaximumFractionDigits(2);
            String str=Nformat.format(police);
            //取两位小数
            Map<String,Object> map = new HashMap<>();
            map.put("message",df.format(new Date())+" "+list1.get(i).get("name")+" " +str);
            int length=list1.size()-1;
            if(i!=0&&i!=length) {
                map.put("type", "一般风险");
                map.put("dLevel","2");
            }
            else if(i==0){
                map.put("type", "高风险");
                map.put("dLevel","1");
            }
            else if(i==length) {
                    map.put("type", "低风险");
                map.put("dLevel","3");
            }
            list.add(map);
        }

        return list;
    }
@Override
    public Map<String, Object> getBaoRuoNumber(String status){
        //获取各个薄弱节点的数量
        Map<String,Object> map = new HashMap<>();
        if("1".equals(status)){
            map.put("number1",1);
            map.put("number2",4);
            map.put("number3",1);
        }
        else if("2".equals(status)){
            map.put("number1",0);
            map.put("number2",0);
            map.put("number3",0);
        }
        else if("3".equals(status)){
            List<Map<String, Object>> list= kongJianMapper.getBaoJing("2","2");
            int h=0;
            int m=0;
            int l=0;
            for(int i=0;i<list.size();i++){
                if(list.get(i).get("level").toString().equals("高风险")){
                    h++;
                }
                else if(list.get(i).get("level").toString().equals("一般风险")){
                    m++;
                }
                else if(list.get(i).get("level").toString().equals("低风险")){
                    l++;
                }
            }
            map.put("number1",h);
            map.put("number2",m);
            map.put("number3",l);
        }
        //最薄弱
        return map;
    }
    public List<Map<String, Object>> getBoRuo(String area){
        //获取薄弱节点以及裕度
        List<Map<String,Object>> list1 = kongJianMapper.getBoRuo(area);
        List<Map<String,Object>> list = new ArrayList<>();
        Nformat.setMaximumFractionDigits(2);
        for(int i=0;i<list1.size();i++){
            Map<String,Object> map = new HashMap<>();
            map.put("nodeName",list1.get(i).get("name"));
            double police = Double.parseDouble((list1.get(i).get("before")).toString());
            map.put("number",Nformat.format(police));
            list.add(map);
        }
        return list;
    }
    public List<Map<String,Object>> getYuXiang()
    {
        //模拟态->区域
        //获取预想故障下区域信息
        List<Map<String,Object>> list = new ArrayList<>();
        List<String> locationList=new ArrayList<>();
        locationList.add("国调.峡葛III线");
        locationList.add("华中.南香I回线");
        locationList.add("国调.峡林I线");
        locationList.add("河南.Ⅲ灵朝线");
        locationList.add("河南.Ⅱ盘灵线");
        locationList.add("华中.盘龙ⅠB线");
        locationList.add("国调.南洲I线");
        locationList.add("华中.葛岗线");
        List<String> typeList=new ArrayList<>();
        typeList.add("A相短路故障");
        typeList.add("A相短路故障");
        typeList.add("AB两相短路故障");
        typeList.add("AB两相短路故障");
        typeList.add("单相短路故障");
        typeList.add("单相短路故障");
        typeList.add("单相短路故障");
        typeList.add("单相短路故障");
        //"故障位置、故障类型、稳定状态"
        for(int i=0;i<locationList.size();i++){
            Map<String,Object> map = new HashMap<>();
            map.put("name","第"+String.valueOf(i+1)+"类故障");
            map.put("line",locationList.get(i));
            map.put("type",typeList.get(i));
            map.put("time","0.1s");
            list.add(map);
        }
        return list;
    }
    public List<Map<String,Object>> getTiaoKongBoRuo()
    {
        //模拟态->区域
        //调控后的薄弱点
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        //"名称、调控前裕度、调控后裕度"
        map.put("name","吉林");
        map.put("before","0.1");
        map.put("after","0.6");
        list.add(map);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("name","黑龙江");
        map1.put("before","0.1");
        map1.put("after","0.6");
        list.add(map1);
        return list;
    }
    public Map<String, Object> getKongJInfo1(String area,String isStatic) {
         //实时态
        Map<String,Object> resultData =new HashMap<>();
        //获取五个仪表盘的数据
        if("全国".equals(area)){
            resultData.put("data1",getBaoJing(area));//获取报警信息
            resultData.put("data2",getImIndex("全国",isStatic));//获取稳定指标信息

            resultData.put("data4",getAreaInfo());//获取六大区域的震荡频率和阻尼比
            resultData.put("data5",getBoRuo(area));//获取薄弱点信息

            resultData.put("data7",getBaoRuoNumber("1"));//获取薄弱节点各个的数量
            resultData.put("data9","");
        }
        else if(StaticResource.AREA_Set.contains(area)){
            resultData.put("data1",getBaoJing(area));//获取报警信息
            resultData.put("data2",getImIndex(area,isStatic));//获取区域下省份的稳定指标信息
            resultData.put("data4","");
            resultData.put("data5",getBoRuo(area));
            resultData.put("data9",getAreaZDandZN(area));//获取模式，震荡频率，阻尼比
        }
        else if(StaticResource.PROVINCE_Set.contains(area)){
            resultData.put("data1",getBaoJing(area));//获取报警信息
            resultData.put("data2",getImIndex(area,isStatic));//获取区域下省份的稳定指标信息
            resultData.put("data4","");
            resultData.put("data5",getBoRuo(area));
            resultData.put("data9",getAreaZDandZN(area));//获取模式，震荡频率，阻尼比
        }
        else if(StaticResource.CITY_SET.contains(area)){
            resultData.put("data1",getBaoJing(area));//获取报警信息
            resultData.put("data2",getImIndex("哈尔滨市",isStatic));//获取区域下省份的稳定指标信息

            resultData.put("data4","");
            resultData.put("data5",getBoRuo(area));
            resultData.put("data9",getAreaZDandZN(area));
            //获取模式，震荡频率，阻尼比
        }
        else
        {
            resultData.put("error",errorResult(area));
            return resultData;
        }
        resultData.put("data0",getIndex()); //获取仪表盘信息
        resultData.put("data3",getControlPolice());//获取设备调控策略公用的

        resultData.put("data7",getBaoRuoNumber("1"));
        resultData.put("data11","");
        resultData.put("data10","");
        resultData.put("data12","");
        return resultData;

    }

    public Map<String, Object> getKongJInfo2(String area) {
        //模拟态
        Map<String,Object> resultData =new HashMap<>();
        if("全国".equals(area)){
            resultData.put("data1",getBaoJing(area));//获取报警信息 左三
            resultData.put("data2",getImIndex("全国","1"));//获取稳定指标信息

            resultData.put("data4","");
            resultData.put("data5",getBoRuo(area));//获取薄弱点信息//左一
            resultData.put("data7",getBaoRuoNumber("1"));
            resultData.put("data11","");
            resultData.put("data12","");
        }
        else if(StaticResource.AREA_Set.contains(area)){
            resultData.put("data1",getGuZhangBaoJing("2","1"));//获取报警信息
            resultData.put("data2",getImIndex(area,"1"));//获取区域下省份的稳定指标信息

            resultData.put("data4","模拟态区域");
            resultData.put("data5",getBoRuo(area));
            resultData.put("data7",getBaoRuoNumber("2"));
            resultData.put("data11",getYuXiang());
            resultData.put("data12",getTiaoKongBoRuo());
        }
        else
        {
            resultData.put("error",errorResult(area));
            return resultData;
        }
        resultData.put("data0",getIndex());
        resultData.put("data3",getControlPolice());
        //获取设备调控策略

        resultData.put("data9","");
        resultData.put("data10","");//区域故障信息 散点图 右二 在controlSocket里写了
        return resultData;
    }
    @Override
    public Map<String, Object> getKongJInfo(String message) {
        JSONObject object=JSONObject.parseObject(message);
        String type=object.getString("type");
        String area=object.getString("area");
        String JZStatus=object.getString("JZStatus");
        if("1".equals(type)){
            //实时态
            return getKongJInfo1(area,JZStatus);
        }
        else if("2".equals(type)){
            //模拟态
            return getKongJInfo2(area);
        }
        return errorResult(area).get(0);
    }
}
