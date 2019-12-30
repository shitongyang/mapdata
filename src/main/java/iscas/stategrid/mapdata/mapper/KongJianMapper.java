package iscas.stategrid.mapdata.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author yangshitong
 * @Date 2019/12/23 14:01
 * @Version 1.0
 * @Description:
 */
@Mapper
@Component
public interface KongJianMapper {
     List<Map<String,Object>> getBoRuo(String area);
    //获取薄弱点裕度
     List<Map<String,Object>> getDevicePolice();

     //获取模拟态区域报警信息
     List<Map<String,Object>> getBaoJing(String para,String status);
}
