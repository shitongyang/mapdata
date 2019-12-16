package iscas.stategrid.mapdata.util;

import redis.clients.jedis.Jedis;

/**
 * @author : lvxianjin
 * @Date: 2019/10/24 17:45
 * @Description:
 */
public class RedisClient {
    /**
     *
     * 功能描述: 写值
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/25 10:19
     */
    public void setValue(String key,String value){
        Jedis jedis = new Jedis("192.168.101.71",6001);
        jedis.set(key,value);
        jedis.close();
    }
    /**
     *
     * 功能描述: 读值
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/25 10:21
     */
    public String getValue(String key){
        Jedis jedis = new Jedis("192.168.101.71",6001);
        String value = jedis.get(key);
        jedis.close();
        return value;

    }
}
