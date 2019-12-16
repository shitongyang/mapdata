package iscas.stategrid.mapdata.util;

import java.io.*;
import java.util.ArrayList;

/**
 * @author : lvxianjin
 * @Date: 2019/10/25 13:55
 * @Description:
 */
public class FileClient {
    public static ArrayList<String> LoadFile(String path){
        ArrayList<String> list = new ArrayList<>();
        File file = new File(path);
        file.setReadable(true);
        file.setWritable(true);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String line = "";
        String everyline = "";
        try {
            while ((line = reader.readLine())!= null){
                everyline = line;
                list.add(everyline);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {

        ArrayList l=LoadFile("C:\\Users\\user\\Desktop\\市名.txt");


        for(int i=0;i<l.size();i++){
            System.out.println("add("+"\""+l.get(i)+"\""+");");
        }
    }
}
