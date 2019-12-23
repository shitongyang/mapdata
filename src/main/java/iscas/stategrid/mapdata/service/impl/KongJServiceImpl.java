package iscas.stategrid.mapdata.service.impl;
import com.alibaba.fastjson.JSONObject;
import iscas.stategrid.mapdata.service.KongJService;
import iscas.stategrid.mapdata.mapper.LocationMapper;
import iscas.stategrid.mapdata.util.FileClient;
import iscas.stategrid.mapdata.util.StaticResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static iscas.stategrid.mapdata.util.StaticResource.errorResult;

/**
 * @author : lvxianjin
 * @Date: 2019/10/23 15:59
 * @Description: 控件信息
 */
@Service
public class KongJServiceImpl implements KongJService {

    NumberFormat Nformat = NumberFormat.getInstance();
    /*
    * 文件名
    * */
    private int count = 1;
    /*
     * 安全性指标
     * */
    private int index_1 = 0;
    /*
     * 经济型指标
     * */
    private int index_2 = 0;
    /*
     * 综合指标
     * */
    private int index_3 = 0;
    /*
     * 静态安全分析指标
     * */
    private int index_4 = 0;
    /*
     * 暂态安全分析指标
     * */
    private int index_5 = 0;
    /*
    * 薄弱节点id
    * */
    List<Integer> ids = new ArrayList<>();
    /*
    * 策略表信息
    * */
    List<Map<String, String>> data2_list = new ArrayList<>();
    /*
    * 六大地区振荡频率和阻尼比信息
     */
    List<Map<String, String>> area_info = new ArrayList<>();

    @Autowired
    private LocationMapper stLocationEntityMapper;

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
        index_1 = (int) (Math.random() * (85 - 30) + 30);
        index_2 = (int) (Math.random() * (85 - 30) + 30);
        index_3 = (int) (Math.random() * (85 - 30) + 30);
        index_4 = (int) (Math.random() * (85 - 30) + 30);
        index_5 = (int) (Math.random() * (85 - 30) + 30);
        info.put("index_1", String.valueOf(index_1));
        info.put("index_2", String.valueOf(index_2));
        info.put("index_3", String.valueOf(index_3));
        info.put("index_4", String.valueOf(index_4));
        info.put("index_5", String.valueOf(index_5));
        return info;
    }
    @Override
    public String getIndexByFlag(String flag) {
        String index = "";
        if(flag.equals("1")){
            index = "百分之"+index_1;
        }else if(flag.equals("2")){
            index = "百分之"+index_3;
        }else if(flag.equals("3")){
            index = "百分之"+index_2;
        }else if(flag.equals("4")){
            index = "百分之"+index_4;
        }else {
            index = "百分之"+index_5;
        }
        return index;
    }

    @Override
    public List<Map<String, String>> getAreaInfo() {
        String rootPath = "C:\\Users\\lvxianjin\\Desktop\\设计稿四\\result\\";
        FileClient fileClient = new FileClient();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        area_info = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Map<String, String> map = new HashMap<>();
            if (i == 0) {
                map.put("areaName", "华北");
            } else if (i == 1) {
                map.put("areaName", "华东");
            } else if (i == 2) {
                map.put("areaName", "华中");
            } else if (i == 3) {
                map.put("areaName", "东北");
            } else if (i == 4) {
                map.put("areaName", "西北");
            } else if (i == 5) {
                map.put("areaName", "西南");
            }
            String filePath = rootPath+String.valueOf(i)+"\\"+String.valueOf(count)+".txt";
            List<String> content = fileClient.getContent(filePath);
            String str_hz = content.get(1).split(",")[1];
            String str_percent = content.get(1).split(",")[0];
            double percent = Double.parseDouble(str_percent)*100;
            double hz = Double.parseDouble(str_hz);
            NumberFormat Nformat = NumberFormat.getInstance();
            // 设置小数位数。
            Nformat.setMaximumFractionDigits(2);
            map.put("hz",Nformat.format(hz));
            map.put("percent",Nformat.format(percent)+"%");
            map.put("time", df.format(new Date()));
            area_info.add(map);
        }
        count = count+1;
        if(count == 4){
            count = 1;
        }
        return area_info;
    }

    @Override
    public String getHZByArea(String area) {
        String message = "";
        for (int i = 0; i <area_info.size() ; i++) {
            Map<String,String> map = area_info.get(i);
            System.out.println(area);
            if(map.get("name").equals(area)){
                message = map.get("hz");
            }
        }
        return message;
    }

    @Override
    public String getZNByArea(String area) {
        String message = "";
        for (int i = 0; i <area_info.size() ; i++) {
            Map<String,String> map = area_info.get(i);
            if(map.get("name").equals(area)){
                message = map.get("percent");
            }
        }
        return message;
    }

    @Override
    public String getPoliceInfo() {
        String police = "";
        for (int i = 0; i <data2_list.size() ; i++) {
            Map<String,String> map = data2_list.get(i);
            police = police+map.get("station_name")+map.get("FDJ_name")+"调控量"+map.get("Police")+";";
        }
        return police;
    }

    @Override
    public List<Map<String, String>>getImIndex(String area,String isStatic) {
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



    public List<Map<String, String>> getControlPolice() {
        //获取设备调控策略
        List<Map<String,String>> list = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        ids.add("1#发电机");
        ids.add("2#发电机");
        ids.add("3#发电机");
        ids.add("4#发电机");
        NumberFormat Nformat = NumberFormat.getInstance();
        // 设置小数位数
        Nformat.setMaximumFractionDigits(2);
        int length=ids.size();
        for (int i = 0; i <length ; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("deviceName",ids.get(i));
            double d = ((int) (Math.random() * (60 - 40) + 40)) * 0.01;
            map.put("index",Nformat.format(d));
            list.add(map);
        }
        return list;
    }

    public List<Map<String, String>> getDeviceMotaiInfo(String area) {
        //获取设备模态信息
        List<Map<String,String>> list = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        ids.add("device1");
        ids.add("device2");
        ids.add("device3");
        ids.add("device4");
        ids.add("device5");
        ids.add("device6");
        NumberFormat Nformat = NumberFormat.getInstance();
        // 设置小数位数
        Nformat.setMaximumFractionDigits(2);
        int length=ids.size();
        for (int i = 0; i <length ; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("deviceName",ids.get(i));
            double d = (int) (Math.random() * (60 - 40));

            if(i%2==0){
                d=0-d;
                //取负数
            }
            map.put("value",Nformat.format(d));
            list.add(map);
        }
        return list;
    }

    public List<Map<String,Object>> getAreaZDandZN(String area)
    {
        //获得区域的震荡频率和阻尼比
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("modelName","模式1");
        map.put("hz","0.1");
        map.put("percent","0.6");
        list.add(map);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("modelName","模式2");
        map1.put("hz","0.5");
        map1.put("percent","0.2");
        list.add(map1);
        return list;
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

    public List<Map<String, Object>> getBaoJing(List<String> list1,String isStatic){
        //获取报警信息
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        List<Map<String,Object>> list = new ArrayList<>();
        for(int i=0;i<list1.size();i++){
            double police = ((int) (Math.random() * (8 -5 ) + 5)) * 0.1;

            if("2".equals(isStatic)){
                police=((int) (Math.random() * (6 -4 ) + 4)) * 0.1;
                //暂态时的指标
            }
            Nformat.setMaximumFractionDigits(2);
            String str=Nformat.format(police);
            //取两位小数
            Map<String,Object> map = new HashMap<>();
            map.put("message",df.format(new Date())+" "+list1.get(i)+" " +str);
            if(i%3==0) {
                map.put("type", "薄弱");
            }
            else if(i%3==1){
                map.put("type", "较薄弱");
            }
            else if(i%3==2) {
                map.put("type", "最薄弱");
            }
            list.add(map);
        }

        return list;
    }
    public Map<String, Object> getBaoRuoNumber(){
        //获取各个薄弱节点的数量
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("number1",1);
        map.put("number2",1);
        map.put("number3",1);
        //最薄弱
        return map;
    }
    public List<Map<String, Object>> getBoRuo(List<String> list1){
        //获取全国的薄弱节点
        List<Map<String,Object>> list = new ArrayList<>();
        for(int i=0;i<list1.size();i++){
            double police = ((int) (Math.random() * (8 - 2) + 2)) * 0.01;
            Map<String,Object> map = new HashMap<>();
            map.put("nodeName",list1.get(i));
            map.put("number",police);
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
        locationList.add("国调峡葛III线");
        locationList.add("华中 南香I回线");
        locationList.add("团林换流站");
        List<String> typeList=new ArrayList<>();
        typeList.add("A相单相永久短路故障");
        typeList.add("A相单相永久短路故障");
        typeList.add("AB相两相永久短路故障");
        //"故障位置、故障类型、稳定状态"
        for(int i=0;i<locationList.size();i++){
            Map<String,Object> map = new HashMap<>();
            map.put("location",locationList.get(i));
            map.put("type",typeList.get(i));
            map.put("status","");
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
    public Map<String, Object> getKongJInfo1(String area,String isStatic,String model) {
         //实时态
        Map<String,Object> resultData =new HashMap<>();
        //获取五个仪表盘的数据
        if("全国".equals(area)){
            List<String> list1=new ArrayList<>();
            list1.add("国调龙泉换流站");
            list1.add("华东东明");
            list1.add("国调灵宝换流站");
            resultData.put("data1",getBaoJing(list1,isStatic));//获取报警信息
            resultData.put("data2",getImIndex("全国",isStatic));//获取稳定指标信息

            resultData.put("data4",getAreaInfo());//获取六大区域的震荡频率和阻尼比
            resultData.put("data5",getBoRuo(list1));//获取薄弱点信息
            resultData.put("data6","热力图,暂无");//热力图
            resultData.put("data7",getBaoRuoNumber());//获取薄弱节点各个的数量
            resultData.put("data8","");
            resultData.put("data9","");
            resultData.put("data10","");
        }
        else if(StaticResource.AREA_Set.contains(area)){
            List<String> list1=new ArrayList<>();
            list1.add("吉林荣家变");
            list1.add("辽宁岭南站");
            list1.add("黑龙江兴安站");
            resultData.put("data1",getBaoJing(list1,isStatic));//获取报警信息
            resultData.put("data2",getImIndex(area,isStatic));//获取区域下省份的稳定指标信息
            resultData.put("data4","");
            resultData.put("data5","");
            resultData.put("data6","热力图,暂无");//热力图，暂无
            resultData.put("data7",getBaoRuoNumber());
            resultData.put("data8",getDeviceMotaiInfo(area));//获取模式，震荡频率，阻尼比，与data9联动
            resultData.put("data9",getAreaZDandZN(area));//获取模式，震荡频率，阻尼比
            resultData.put("data10","");
        }
        else if(StaticResource.PROVINCE_Set.contains(area)){
            List<String> list1=new ArrayList<>();
            list1.add("黑龙江城乡站");
            list1.add("黑龙江尚牵站");
            list1.add("黑龙江繁荣站");
            resultData.put("data1",getBaoJing(list1,isStatic));//获取报警信息
            resultData.put("data2",getImIndex(area,isStatic));//获取区域下省份的稳定指标信息
            resultData.put("data4","");
            resultData.put("data5","");
            List<String> list6=stLocationEntityMapper.selectCityByProvince(area);
            System.out.println(list6);
            resultData.put("data6","热力图,暂无");
            resultData.put("data7",getBaoRuoNumber());
            resultData.put("data8",getDeviceMotaiInfo(area));//获取模态信息与data9联动
            resultData.put("data9",getAreaZDandZN(area));//获取模式，震荡频率，阻尼比
            resultData.put("data10",getWeather(stLocationEntityMapper.selectCityByProvince(area)));//获取天气状况
        }
        else if(StaticResource.CITY_SET.contains(area)){
            List<String> list1=new ArrayList<>();
            list1.add("爱华风电厂");
            list1.add("和平风电厂");
            list1.add("穆牵站");
            resultData.put("data1",getBaoJing(list1,isStatic));//获取报警信息
            resultData.put("data2",getImIndex("哈尔滨市",isStatic));//获取区域下省份的稳定指标信息

            resultData.put("data4","");
            resultData.put("data5","");
            resultData.put("data6","");
            resultData.put("data7",getBaoRuoNumber());
            resultData.put("data8",getDeviceMotaiInfo(area));
            //获取模态信息与data9联动
            resultData.put("data9",getAreaZDandZN(area));
            //获取模式，震荡频率，阻尼比
            resultData.put("data10",getWeather(stLocationEntityMapper.selectCountyByCity(area)));
            //获取天气状况
        }
        else
        {
            resultData.put("error",errorResult(area));
            return resultData;
        }
        resultData.put("data0",getIndex()); //获取仪表盘信息
        resultData.put("data3",getControlPolice());//获取设备调控策略公用的
        resultData.put("data11","");
        resultData.put("data12","");
        return resultData;

    }

    public Map<String, Object> getKongJInfo2(String area) {
        //模拟态
        Map<String,Object> resultData =new HashMap<>();
        if("全国".equals(area)){
            List<String> list1=new ArrayList<>();
            list1.add("国调龙泉换流站");
            list1.add("华东东明");
            list1.add("国调灵宝换流站");
            resultData.put("data1",getBaoJing(list1,"1"));//获取报警信息
            resultData.put("data2",getImIndex("全国","1"));//获取稳定指标信息

            resultData.put("data4",getAreaInfo());//获取六大区域的震荡频率和阻尼比
            resultData.put("data5",getBoRuo(list1));//获取薄弱点信息
            resultData.put("data6","热力图,暂无");//热力图
            resultData.put("data7",getBaoRuoNumber());

            resultData.put("data11","");
            resultData.put("data12","");
        }
        else if(StaticResource.AREA_Set.contains(area)){
            List<String> list1=new ArrayList<>();
            list1.add("吉林荣家变");
            list1.add("辽宁岭南站");
            list1.add("黑龙江兴安站");
            resultData.put("data1",getBaoJing(list1,"1"));//获取报警信息
            resultData.put("data2",getImIndex(area,"1"));//获取区域下省份的稳定指标信息

            resultData.put("data4",getDeviceMotaiInfo(area));
            //获取模态信息与data5联动
            resultData.put("data5",getAreaZDandZN(area));
            //获取模式，震荡频率，阻尼比
            resultData.put("data6","热力图,暂无");
            //热力图，暂无
            resultData.put("data7",getBaoRuoNumber());

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
        resultData.put("data8","");
        resultData.put("data9","");
        resultData.put("data10","");
        return resultData;
    }
    @Override
    public Map<String, Object> getKongJInfo(String message) {
        JSONObject object=JSONObject.parseObject(message);
        System.out.println(object);
        String type=object.getString("type");
        String area=object.getString("area");
        String JZStatus=object.getString("JZStatus");
        String model=object.getString("model");
        if("1".equals(type)){
            //实时态
            return getKongJInfo1(area,JZStatus,model);
        }
        else if("2".equals(type)){
            //模拟态
            return getKongJInfo2(area);
        }
        return errorResult(area).get(0);
    }
}
