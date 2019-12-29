package iscas.stategrid.mapdata.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/12/19 14:22
 * @Description:
 */
@Mapper
@Component
public interface VoiceDao {
    public Map<String,String> getErrorInfo(String id);
    public List<Map<String,String>> getLineInfo(String name);
    public Map<String,String> getLocationByName(String name);
    public List<Map<String,String>> getWeak(String area);
    public String getGroup(String time,String member);
}
