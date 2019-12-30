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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(df.format(new Date()));

    }
}
