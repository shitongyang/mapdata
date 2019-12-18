package iscas.stategrid.mapdata.service;

import java.util.List;
import java.util.Map;

public interface WinPositionService {
    //全国风电机组的位置
    List<Map<String,String>> getWinPosition();
    //地区某风电机组下各个风电机的位置与连接情况
    Map<String,List<Map<String,String>>> getWinLine();
    //区域展示电压、有功功率、无功对比折线
    List<Map<String,String>> getDYWInfo();
}
