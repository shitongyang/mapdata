package iscas.stategrid.mapdata.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author yangshitong
 * @Date 2019/12/12 13:30
 * @Version 1.0
 * @Description:
 */

public class StringToJson {
    public static void main(String[] args) {
        String str = "{" + "\"" + "latitude" + "\"" + ":" + 30.23 + "," + "\"" + "longitude"
                + "\"" + ":" + 114.57 + "}";
        JSONObject object=JSONObject.parseObject(str);
        System.out.println(object);
        String s=object.getString("latitud");
        System.out.println(""==object.get("latitud"));
        System.out.println((int)(Math.random()*5)+30);

        Map<String,Object> map = new HashMap<>();
        map.put("age", 24);
        map.put("name", "cool_summer_moon");
        System.out.println(JSON.toJSONString(map));
        JSONObject json = new JSONObject(map);
        String array[]="2,2".split(",");
        System.out.println(array[0]=="2");
        System.out.println(new Date());

        double d = (int) (Math.random() * (60 - 40));
        System.out.println(0-d);

    }
}
