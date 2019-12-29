package iscas.stategrid.mapdata.controller;

import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.service.KongJService;
import iscas.stategrid.mapdata.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class HelloController {
    @Autowired
    private MapService mapService;
    @Autowired
    private KongJService kongJService;

    @RequestMapping(value="/getLocation",method = RequestMethod.GET)
    public String getLocation(@RequestParam("location") String location){
        return JSON.toJSONString(mapService.getLocation(location));
    }

    @RequestMapping(value="/getWeakLocation",method = RequestMethod.GET)
    public String getWeakLocation(@RequestParam("weakLocation") String weakLocation){
        return JSON.toJSONString(mapService.getWeakLocation(weakLocation));
    }

    @RequestMapping(value="/getModelInfo",method = RequestMethod.GET)
    public String getModelInfo(@RequestParam("area") String area,@RequestParam(defaultValue = "模式1") String modelName){
        return JSON.toJSONString(kongJService.getDeviceMotaiInfo(area,modelName));
    }
    @RequestMapping(value="/getAreaModel",method = RequestMethod.GET)
    public String getAreaModel(@RequestParam("area") String area){
        return JSON.toJSONString(kongJService.getAreaZDandZN(area));
    }
}
