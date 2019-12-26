package iscas.stategrid.mapdata.test;

import java.util.Calendar;

/**
 * @Author yangshitong
 * @Date 2019/12/26 10:12
 * @Version 1.0
 * @Description:
 */

public class testRandom {

    public static void main(String[] args) {
        System.out.println((int)(Math.random()*10)+20);
        Calendar calendar=Calendar.getInstance();
        int currentHour24=calendar.get(calendar.HOUR_OF_DAY);
        System.out.println(currentHour24+"h");
        System.out.println((int)(Math.random()*8)+1+"h");
    }
}
