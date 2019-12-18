package iscas.stategrid.mapdata.controller;

import iscas.stategrid.mapdata.service.WinPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class WinPositionController {
    @Autowired
    private WinPositionService winPositionService;

    //全国风电机组的位置
    @RequestMapping(value = "getWP.json",method = RequestMethod.GET)
    public List<Map<String,String>> getWP(){
        List<Map<String,String>> message_WP = winPositionService.getWinPosition();
        return message_WP;
    }

    //地区某风电机组下各个风电机的位置与连接情况
    @RequestMapping(value = "getWL.json",method = RequestMethod.GET)
    public Map<String,List<Map<String,String>>> getWL(){
        Map<String,List<Map<String,String>>> message_WL = winPositionService.getWinLine();
        return message_WL;
    }

}
