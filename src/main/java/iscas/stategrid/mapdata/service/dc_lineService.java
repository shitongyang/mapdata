package iscas.stategrid.mapdata.service;

import java.util.List;
import java.util.Map;

public interface dc_lineService {

    List<Map<String,Object>> getTopoLine(String area);
    List<Map<String,Object>> getTopoLocation(String area);
    List<Map<String,Object>> getLocation(String area);
}
