package iscas.stategrid.mapdata.controller;

import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class HelloController {


    @Autowired
    private MapService mapService;

    @RequestMapping(value="/getLocation",method = RequestMethod.GET)
    public String getLocation(@RequestParam("location") String location){
        return JSON.toJSONString(mapService.getLocation(location));
    }

    @RequestMapping(value="/getWeakLocation",method = RequestMethod.GET)
    public String getWeakLocation(@RequestParam("weakLocation") String weakLocation){
        return JSON.toJSONString(mapService.getWeakLocation(weakLocation));
    }


}
