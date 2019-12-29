package iscas.stategrid.mapdata.service;

import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/10/24 09:02
 * @Description: 语音业务逻辑处理
 */
public interface VoiceService {
    /**
     *
     * 功能描述: 语音获取实时信息
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/27 21:39
     */
    String queryCommand(String commandType, String area);
    /**
     *
     * 功能描述: 语音操作大屏
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/28 8:39
     */
    String operate(String commandType, String parameter);
    /**
     *
     * 功能描述: 获取是否进行调控
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/12/18 9:28
     */
    public boolean getControl();
}
