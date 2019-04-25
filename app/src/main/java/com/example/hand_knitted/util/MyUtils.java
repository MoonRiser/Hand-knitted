package com.example.hand_knitted.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUtils {



    public static final String[] tool = new String[]{"棒织","编织","钩织结合"};
    public static final String[] group = new String[]{"儿童","女士","男式","婴幼儿","老年人"};
    public static final String[] style = new String[]{"麻花","提花","雪花","流苏","叶子花"};


    //获取系统当前时间/以字符串形式存储
    public static String getCurrentDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //   Log.w("date到底有没有被规格化？", str);
        return simpleDateFormat.format(date);
    }
}
