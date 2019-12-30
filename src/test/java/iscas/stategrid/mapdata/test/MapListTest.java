package iscas.stategrid.mapdata.test;

import iscas.stategrid.mapdata.service.impl.MapServiceImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yangshitong
 * @Date 2019/12/20 10:16
 * @Version 1.0
 * @Description:
 */

public class MapListTest {
    public static void main(String[] args) {
        Map<String, List<Map<String,Object>>> testMap=new HashMap<>();
        System.out.println(testMap);
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        map.put("11",33);
        System.out.println(map.get("111"));
        Map<String,Object> map1=new HashMap<>();
        map1.put("11",33);
        System.out.println(map.equals(map1));
        testMap.put("1",list);
        testMap.put("1",null);
        testMap.remove("2");
        System.out.println(testMap);

    }

}
