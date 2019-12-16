package iscas.stategrid.mapdata.Service.impl;

import com.alibaba.fastjson.JSONObject;
import iscas.stategrid.mapdata.Service.dc_lineService;
import iscas.stategrid.mapdata.mapper.st_locationEntityMapper;
import iscas.stategrid.mapdata.util.StaticResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import static iscas.stategrid.mapdata.util.StaticResource.errorResult;

@Service
public class dc_lineImpl implements dc_lineService {

    @Autowired
    private st_locationEntityMapper locationEntitymapper;
    //获得拓扑线路
    @Override
    public List<Map<String, Object>> getTopoLine(String area) {
        JSONObject object=JSONObject.parseObject(area);
            String quyu=object.getString("area");
            if ("全国".equals(quyu)) {
                return locationEntitymapper.selectGlobalTopo();
            } else if (StaticResource.AREA_Set.contains(quyu)) {
                List<Map<String, Object>> list = locationEntitymapper.selectAreaTopo(quyu ,"1000kv");
                if (list == null || list.size() <10) {
                    list = locationEntitymapper.selectAreaTopo(quyu ,"750kv");
                }
                if (list == null || list.size() <10) {
                    list = locationEntitymapper.selectAreaTopo(quyu ,"500kv");
                }
                return list;

            } else if (StaticResource.PROVINCE_Set.contains(quyu)) {
                System.out.println("area:1111111111111" + quyu);
                List<Map<String, Object>> list = locationEntitymapper.selectProvinceTopo(quyu, "500kv");
                if (list == null || list.size() == 0) {
                    list = locationEntitymapper.selectProvinceTopo(quyu, "330kv");
                }
                if (list == null || list.size() == 0) {
                    list = locationEntitymapper.selectProvinceTopo(quyu, "220kv");
                }
                return list;
            }
            else if (StaticResource.CITY_SET.contains(quyu)) {
                String level=object.getString("vlevel")+"kv";
                System.out.println("city:1111111111111" + quyu);
                List<Map<String, Object>> list = locationEntitymapper.selectCityTopo(quyu,level);
                return list;


        }
        else {
            return errorResult(area);
        }
    }
    //获得拓扑结构中的站点信息
    @Override
    public List<Map<String, Object>> getTopoLocation(String area) {
        JSONObject object=JSONObject.parseObject(area);
            String quyu=object.getString("area");
            String isStatic=object.getString("JZStatus");
            if("全国".equals(quyu)){
                List<Map<String, Object>> list=locationEntitymapper.selectGlobalTopoLocation("");
                if(isStatic.equals("2")){
                    //暂态
                    for(int i=0;i<list.size();i++){
                        list.get(i).put("height",(int)(Math.random()*7)+30);
                    }
                }
                return list;
            }
            else if(StaticResource.AREA_Set.contains(quyu)){
                List<Map<String, Object>> list=locationEntitymapper.selectAreaTopoLocation(quyu,"1000kv");
                if(list==null||list.size()<10){
                    list=locationEntitymapper.selectAreaTopoLocation(quyu,"750kv");
                }
                if(list==null||list.size()<10) {
                    list=locationEntitymapper.selectAreaTopoLocation(quyu,"500kv");
                }
                if(isStatic.equals("2")){
                    //暂态
                    for(int i=0;i<list.size();i++){
                        list.get(i).put("height",(int)(Math.random()*7)+30);
                    }
                }
                return list;
            }
            else if(StaticResource.PROVINCE_Set.contains(quyu)){
                System.out.println("province:"+quyu);
                List<Map<String, Object>> list=locationEntitymapper.selectProvinceTopoLocation(quyu,"500kv");
                if(list==null||list.size()==0){
                    list=locationEntitymapper.selectProvinceTopoLocation(quyu,"330kv");
                }
                if(list==null||list.size()==0){
                    list=locationEntitymapper.selectProvinceTopoLocation(quyu,"220kv");
                }
                if(isStatic.equals("2")){
                    //暂态
                    for(int i=0;i<list.size();i++){
                        list.get(i).put("height",(int)(Math.random()*7)+30);
                    }
                }
                return list;
            }
            if (StaticResource.CITY_SET.contains(quyu)) {
                System.out.println("city:1111111111111" + quyu);
                String level=object.getString("vlevel");
                List<Map<String, Object>> list = locationEntitymapper.selectCityTopoLocation(quyu,level);
                if(isStatic.equals("2")){
                    //暂态
                    for(int i=0;i<list.size();i++){
                        list.get(i).put("height",(int)(Math.random()*7)+30);
                    }
                }
                return list;
            }
            else{
                return errorResult(area);
            }
    }
    //获取场站
    @Override
    public List<Map<String, Object>> getLocation(String area) {

        if("全国".equals(area)){
            return locationEntitymapper.selectLocation("");
        }
        else if(StaticResource.AREA_Set.contains(area)){
            return locationEntitymapper.selectAreaLocation(area);
        }
        else if(StaticResource.PROVINCE_Set.contains(area)){
               return locationEntitymapper.selectProvinceLocation(area);
        }
        else if(StaticResource.CITY_SET.contains(area)){
            return locationEntitymapper.selectCityLocation(area);
        }
        else{
            return errorResult(area);
        }
    }
}
