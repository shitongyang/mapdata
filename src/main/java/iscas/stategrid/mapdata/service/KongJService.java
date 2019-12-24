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
 List<Map<String, String>> getAreaInfo();
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
 * 功能描述: 获取关键节点指标信息
 *
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
 //level表示全国，区域，省，市
 public String getHZByArea(String area);
}

