package iscas.stategrid.mapdata.mapper;

import iscas.stategrid.mapdata.domain.st_locationEntity;
import iscas.stategrid.mapdata.domain.st_locationEntityExample;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface st_locationEntityMapper {
    long countByExample(st_locationEntityExample example);
    int deleteByPrimaryKey(Integer id);
    int insert(st_locationEntity record);
    int insertSelective(st_locationEntity record);
    st_locationEntity selectByPrimaryKey(Integer id);
    //List<st_locationEntity> selectAll();
    int updateByPrimaryKeySelective(st_locationEntity record);
    int updateByPrimaryKey(st_locationEntity record);

    //获得全国的拓扑结构
    List<Map<String,Object>> selectGlobalTopo();

    //获得区域的拓扑
    List<Map<String,Object>> selectAreaTopo(String area,String level);

    //获得省的拓扑
    List<Map<String,Object>> selectProvinceTopo(String area,String level);

    //获得市的拓扑
    List<Map<String,Object>> selectCityTopo(String city,String level);


    //查询站点
    List<Map<String,Object>> selectLocation(String area);

    //查询区域的站点
    List<Map<String,Object>> selectAreaLocation(String area);

    //查询省的站点
    List<Map<String,Object>> selectProvinceLocation(String area);

    //查询市的站点
    List<Map<String,Object>> selectCityLocation(String area);

    //查询全国拓扑网络中的站点
    List<Map<String,Object>> selectGlobalTopoLocation(String area);

    //查询区域拓扑网络中的站点
    List<Map<String,Object>> selectAreaTopoLocation(String area,String level);

    //查询省拓扑结构的站点
    List<Map<String,Object>> selectProvinceTopoLocation(String area,String level);

    //查询市拓扑结构的站点
    List<Map<String,Object>> selectCityTopoLocation(String city,String level);

    //12-13,获取省份的拼音
    Map<String,Object> selectPinyinOfProvince(String province);

    //12-14,获取错误节点用于调控
    String getNameById(String id);

    //根据省份获取城市
    List<String> selectCityByProvince(String province);

    //根据城市获取区
    List<String> selectCountyByCity(String city);

    //根据区域获取省份
    List<String> selectProvinceByArea(String area);
}