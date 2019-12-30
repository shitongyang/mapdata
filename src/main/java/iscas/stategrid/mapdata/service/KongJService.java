package iscas.stategrid.mapdata.service;

import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/10/23 15:55
 * @Description:
 */
public interface KongJService {
   /**
    *
    * 功能描述: 获取薄弱节点名称
    *
    * @param:
    * @return:
    * @auther: lvxianjin
    * @date: 2019/10/23 15:57
    */
    List<String> getErrorInfo();
    /**
     *
     * 功能描述: 获取五大指标
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/23 17:08
     */
    Map<String,String> getIndex();
     /**
      *
      * 功能描述: 根据flag获取薄弱节点的裕度
      *
      * @param:
      * @return:
      * @auther: lvxianjin
      * @date: 2019/10/27 21:46
      */
     String getIndexByFlag(String flag);
     /**
      *
      * 功能描述:获取六大地区的信息
      *
      * @param:
      * @return:
      * @auther: lvxianjin
      * @date: 2019/10/28 8:01
      */
 Map<String, Object> getAreaInfo();
 /**
  *
  * 功能描述: 根据地区名称获取振荡频率与阻尼比
  *
  * @param:
  * @return:
  * @auther: lvxianjin
  * @date: 2019/12/24 19:32
  */
 String getModelByArea(String area,String type);
/**
 *
 * 功能描述: 获取关键节点指标信息
 * @param:
 * @return:
 * @auther: lvxianjin
 * @date: 2019/10/28 20:06
 */
List<Map<String,String>> getImIndex(String area,String isStatic);


//获取控件的信息
 //2019/12/14 14:15
 //yangst
 Map<String,Object> getKongJInfo(String message);

 List<Map<String, String>> getDeviceMotaiInfo(String area,String modelName);

List<Map<String,Object>> getAreaZDandZN(String area);

List<Map<String,Object>> getGuZhangBaoJing(String para,String status);
//获取模拟态区域 插入故障后的报警信息
 //status表示有无故障  para表示故障类型

Map<String, Object> getBaoRuoNumber(String status);
}

