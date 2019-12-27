package iscas.stategrid.mapdata.service;

import java.util.List;
import java.util.Map;

/**
 * @Author yangshitong
 * @Date 2019/12/23 20:16
 * @Version 1.0
 * @Description:
 */

public interface MapService {
    List<Map<String,Object>> getTopoLine(String area);
    List<Map<String,Object>> getTopoLocation(String area);
    List<Map<String,Object>> getLocation(String area);
    List<Map<String,String>> getWeakLocation(String area);
}
