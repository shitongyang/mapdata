package iscas.stategrid.mapdata.test;

import java.util.Calendar;

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
    }
}
