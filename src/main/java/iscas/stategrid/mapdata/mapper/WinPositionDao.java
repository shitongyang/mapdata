package iscas.stategrid.mapdata.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface WinPositionDao {

    //全国风电机组的位置
    List<Map<String,String>> getWinPosition();
    //地区某风电机组下各个风电机的位置与连接情况
    Map<String,String> getWinLocation(String id);
    List<Map<String,String>> getWinLine();
    List<Map<String,String>> getWind(String time);
}
