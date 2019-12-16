package iscas.stategrid.mapdata.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import iscas.stategrid.mapdata.Service.dc_lineService;
import iscas.stategrid.mapdata.Service.st_locationService;
import iscas.stategrid.mapdata.domain.st_locationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class HelloController {

    @Autowired
     private st_locationService locationService;
    @Autowired
    private dc_lineService dc_lineService;
    @RequestMapping(value="/get",method = RequestMethod.GET)
    public String get(){
        return "hello zhangli";
    }
    @RequestMapping(value="/getAll",method = RequestMethod.GET)
    public List<st_locationEntity> getAll(){
        return locationService.getAll();
    }

    @RequestMapping(value="/getLocation",method = RequestMethod.GET)
    public String getLocation(@RequestParam("location") String location){
        List<Map<String,Object>> map=dc_lineService.getLocation(location);
        String str = JSON.toJSONString(map);
        return str;
    }


}
