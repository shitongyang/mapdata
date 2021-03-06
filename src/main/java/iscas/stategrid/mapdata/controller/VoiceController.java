package iscas.stategrid.mapdata.controller;

import iscas.stategrid.mapdata.service.VoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : lvxianjin
 * @Date: 2019/10/28 08:34
 * @Description:
 */
@RestController
@CrossOrigin("*")
public class VoiceController {
    @Autowired
    private VoiceService voiceService;
    @RequestMapping(value = "querycommand",method = RequestMethod.POST)
    public String queryCommand(@RequestParam String commandtype, @RequestParam(defaultValue = "null") String area){
        String message = voiceService.queryCommand(commandtype,area);
        //String message = "lvcommandtype:"+commandtype+" area:"+area;
        return message;
    }
    @RequestMapping(value = "operate",method = RequestMethod.POST)
    public String operate(@RequestParam String operationtype, @RequestParam(defaultValue = "null") String parameter){
        System.out.println("operationtype:"+operationtype+" parameter:"+parameter);
        String message = voiceService.operate(operationtype,parameter);
        //voiceService.operate(operationtype,parameter);
        //String message = "lvoperationtype:"+operationtype+" parameter:"+parameter;
        return message;
    }
}
