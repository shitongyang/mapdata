package iscas.stategrid.mapdata.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author yangshitong
 * @Date 2019/12/29 10:24
 * @Version 1.0
 * @Description:
 */

public class DateTimeTest {

    public static void main(String[] args) {
        Calendar calendar=Calendar.getInstance();
       // int currentHour24=calendar.get(calendar.HOUR_OF_DAY);
        int currentHour24=calendar.get(calendar.MINUTE);
        System.out.println((currentHour24+40)%80);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        System.out.println(new Date());
    }
}
