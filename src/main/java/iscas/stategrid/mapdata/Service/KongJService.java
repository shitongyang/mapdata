package iscas.stategrid.mapdata.Service;

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
     * 功能描述: 根据站点名称获取指标信息
     *
     * @param station_name 站点名称
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/24 20:39
     */
    Map<String,String> getDataByName(String station_name);
    /**
     *
     * 功能描述: 根据Id获取指标信息
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/24 20:54
     */
    Map<String,String> getDataById(String id);
     /**
      *
      * 功能描述: 获取模式模态信息
      *
      * @param:
      * @return:
      * @auther: lvxianjin
      * @date: 2019/10/25 13:57
      */
     Map<String, List> getSysMode(String filePath, int count);
     /**
      *
      * 功能描述: 获取决策信息
      *
      * @param:
      * @return:
      * @auther: lvxianjin
      * @date: 2019/10/27 19:20
      */
     Map<String,List> getJCInfo();
     /**
      *
      * 功能描述: 根据id获取薄弱节点的裕度
      *
      * @param:
      * @return:
      * @auther: lvxianjin
      * @date: 2019/10/27 21:45
      */
     Map<String,String> getInfoById(String id);
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
 List<Map<String, String>> getAreaInfo();
 /**
  *
  * 功能描述: 根据地区名称获取振荡频率信息
  *
  * @param:
  * @return:
  * @auther: lvxianjin
  * @date: 2019/10/28 8:07
  */
 String getHZByArea(String area);
 /**
  *
  * 功能描述: 根据地区名称获取阻尼比
  *
  * @param:
  * @return:
  * @auther: lvxianjin
  * @date: 2019/10/28 8:09
  */
 String getZNByArea(String area);
 /**
  *
  * 功能描述: 获取策略表信息
  *
  * @param:
  * @return:
  * @auther: lvxianjin
  * @date: 2019/10/28 8:21
  */
 String getPoliceInfo();
 /**
  *
  * 功能描述: 获取安全概率
  *
  * @param:
  * @return:
  * @auther: lvxianjin
  * @date: 2019/10/28 14:11
  */
Map<String,String> getSecure();
/**
 *
 * 功能描述: 获取关键节点指标信息
 *
 * @param:
 * @return:
 * @auther: lvxianjin
 * @date: 2019/10/28 20:06
 */
List<Map<String,String>> getImIndex();


//获取控件的信息
 //2019/12/14 14:15
 //yangst
 Map<String,Object> getKongJInfo(String level);
 //level表示全国，区域，省，市
}

