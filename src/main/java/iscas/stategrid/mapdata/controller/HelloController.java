package iscas.stategrid.mapdata.controller;

import com.alibaba.fastjson.JSON;
import iscas.stategrid.mapdata.service.dc_lineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class HelloController {


    @Autowired
    private dc_lineService dc_lineService;
    @RequestMapping(value="/get",method = RequestMethod.GET)
    public String get(){
        return "hello zhangli";
    }

    @RequestMapping(value="/getLocation",method = RequestMethod.GET)
    public String getLocation(@RequestParam("location") String location){
        return JSON.toJSONString(dc_lineService.getLocation(location));
    }


}
