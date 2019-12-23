package iscas.stategrid.mapdata.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : lvxianjin
 * @Date: 2019/10/25 13:55
 * @Description:
 */
public class FileClient {
    public int writeResult(List<String> content, String filePath) {
        int rest = 0;
        File file = new File(filePath);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            for (int i = 0; i < content.size(); i++) {
                bw.write(content.get(i) + "\t\n");
            }
            bw.close();
            rest = 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return rest;
    }
    public List<String> getContent(String filePath) {
        List<String> content = new ArrayList<String>();
        BufferedReader br = null;
        File file = new File(filePath);
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
            String str;
            while ((str = br.readLine()) != null) {
                content.add(str);
            }
            br.close();
            ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
