package iscas.stategrid.mapdata.service.impl;

import iscas.stategrid.mapdata.service.WinPositionService;
import iscas.stategrid.mapdata.mapper.WinPositionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WinPositionImpl implements WinPositionService {
    @Autowired
    private WinPositionDao winPositionDao;
    private int count=0;
    private List<Map<String,String>> info=new ArrayList<>();

    //全国风电机组的位置
    @Override
    public List<Map<String, String>> getWinPosition() {
        List<Map<String, String>> wp = winPositionDao.getWinPosition();
        return wp;
    }

    //地区某风电机组下各个风电机的位置与连接情况
    @Override
        public Map<String,List<Map<String,String>>> getWinLine() {
            Map<String,List<Map<String,String>>> info_map = new HashMap<>();
            List<Map<String,String>> data1 = new ArrayList<>();
            List<Map<String,String>> info=winPositionDao.getWinLine();
            for (int i=0;i<info.size();i++){
                Map<String,String> map=new HashMap<>();
                String from=info.get(i).get("from");
                String to=info.get(i).get("to");
                map.put("Flng",winPositionDao.getWinLocation(from).get("lng"));
                map.put("Flat",winPositionDao.getWinLocation(from).get("lat"));
                map.put("Tlng",winPositionDao.getWinLocation(to).get("lng"));
                map.put("Tlat",winPositionDao.getWinLocation(to).get("lat"));
                data1.add(map);
            }
            info_map.put("data1",data1);
            info_map.put("data2",winPositionDao.getWind());
            return info_map;
        }

       List<String> content1=getContent("/jar/model/1.txt");
       List<String> content2=getContent("/jar/model/2.txt");
       List<String> content3=getContent("/jar/model/3.txt");
//           List<String> content1=getContent("C:\\Users\\user\\Desktop\\data\\1.txt");
//           List<String> content2=getContent("C:\\Users\\user\\Desktop\\data\\2.txt");
//           List<String> content3=getContent("C:\\Users\\user\\Desktop\\data\\3.txt");
        public List<String> getContent(String filePath){
            List<String> content=new ArrayList<>();
            BufferedReader br=null;
            File file=new File(filePath);
        try {
            br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"gbk"));
            String str;
            while ((str=br.readLine())!=null){
                content.add(str);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    //区域展示电压、有功功率、无功对比折线
    @Override
    public List<Map<String, String>> getDYWInfo() {
        Map<String,String> map=new HashMap<>();
        map.put("time",content1.get(count).trim().split(",")[0]);
        map.put("WF_v",content1.get(count).trim().split(",")[1]);
        map.put("EQ4_v",content2.get(count).trim().split(",")[1]);
        map.put("EQ1_v",content3.get(count).trim().split(",")[1]);
        map.put("WF_p",content1.get(count).trim().split(",")[2]);
        map.put("EQ4_p",content2.get(count).trim().split(",")[2]);
        map.put("EQ1_p",content3.get(count).trim().split(",")[2]);
        map.put("WF_q",content1.get(count).trim().split(",")[3]);
        map.put("EQ4_q",content2.get(count).trim().split(",")[3]);
        map.put("EQ1_q",content3.get(count).trim().split(",")[3]);
        info.add(map);
        if (info.size()==8){
            info.remove(0);
        }
        count=count+1;
        if (count==800){
            info = new ArrayList<>();
            count=0;
        }
        return info;
    }
}