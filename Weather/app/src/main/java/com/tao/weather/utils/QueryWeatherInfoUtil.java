package com.tao.weather.utils;

import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.tao.weather.bean.WeatherBean;

import java.util.List;
/**
 * Created by Administrator on 2018/11/22.
 */

public class QueryWeatherInfoUtil {

    public static List<WeatherBean.HeWeather5Bean> processData(String json) {
        //解析数据(response);
        WeatherBean weatherBean = JSON.parseObject(json, WeatherBean.class);
        List<WeatherBean.HeWeather5Bean> heWeather5BeanList = weatherBean.getHeWeather5();
        WeatherBean.HeWeather5Bean.BasicBean basicBean = heWeather5BeanList.get(0).getBasic();
        return heWeather5BeanList;
    }

    public static String getStringData() {
        final Calendar calendar = Calendar.getInstance();
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);// 获取当前月份
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String way = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(way)) {
            way = "日";
        } else if ("2".equals(way)) {
            way = "一";
        } else if ("3".equals(way)) {
            way = "二";
        } else if ("4".equals(way)) {
            way = "三";
        } else if ("5".equals(way)) {
            way = "四";
        } else if ("6".equals(way)) {
            way = "五";
        } else if ("7".equals(way)) {
            way = "六";
        }
        return month + "月" + day + "日" + " 周" + way;
    }

}
