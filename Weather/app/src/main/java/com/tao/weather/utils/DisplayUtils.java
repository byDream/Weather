package com.tao.weather.utils;

import android.content.Context;

/**
 * Created by Administrator on 2018/8/25.
 */

public class DisplayUtils {
    /**
     * 将dp转换为px
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px转换为dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px转换为sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp转换为px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getWeatherIcon(String desc) {
        int iconCode = -1;
        switch (desc) {
            case "晴":
                iconCode = 100;
                break;
            case "多云":
                iconCode = 101;
                break;
            case "少云":
                iconCode = 102;
                break;
            case "晴间多云":
                iconCode = 103;
                break;
            case "阴":
                iconCode = 104;
                break;
            case "有风":
                iconCode = 200;
                break;
            case "平静":
                iconCode = 201;
                break;
            case "微风":
                iconCode = 202;
                break;
            case "和风":
                iconCode = 203;
                break;
            case "清风":
                iconCode = 204;
                break;
            case "强风/劲风":
                iconCode = 205;
                break;
            case "疾风":
                iconCode = 206;
                break;
            case "大风":
                iconCode = 207;
                break;
            case "烈风":
                iconCode = 208;
                break;
            case "风暴":
                iconCode = 209;
                break;
            case "狂爆风":
                iconCode = 210;
                break;
            case "飓风":
                iconCode = 211;
                break;
            case "龙卷风":
                iconCode = 212;
                break;
            case "热带风暴":
                iconCode = 213;
                break;
            case "阵雨":
                iconCode = 300;
                break;
            case "强阵雨":
                iconCode = 301;
                break;
            case "雷阵雨":
                iconCode = 302;
                break;
            case "强雷阵雨":
                iconCode = 303;
                break;
            case "雷阵雨伴有冰雹":
                iconCode = 304;
                break;
            case "小雨":
                iconCode = 305;
                break;
            case "中雨":
                iconCode = 306;
                break;
            case "大雨":
                iconCode = 307;
                break;
            case "极端降雨":
                iconCode = 308;
                break;
            case "毛毛雨/细雨":
                iconCode = 309;
                break;
            case "暴雨":
                iconCode = 310;
                break;
            case "大暴雨":
                iconCode = 311;
                break;
            case "特大暴雨":
                iconCode = 312;
                break;
            case "冻雨":
                iconCode = 313;
                break;
            case "小到中雨":
                iconCode = 314;
                break;
            case "中到大雨":
                iconCode = 315;
                break;
            case "大到暴雨":
                iconCode = 316;
                break;
            case "暴雨到大暴雨":
                iconCode = 317;
                break;
            case "大暴雨到特大暴雨":
                iconCode = 318;
                break;
            case "雨":
                iconCode = 399;
                break;
            case "小雪":
                iconCode = 400;
                break;
            case "中雪":
                iconCode = 401;
                break;
            case "大雪":
                iconCode = 402;
                break;
            case "暴雪":
                iconCode = 403;
                break;
            case "雨夹雪":
                iconCode = 404;
                break;
            case "雨雪天气":
                iconCode = 405;
                break;
            case "阵雨夹雪":
                iconCode = 406;
                break;
            case "阵雪":
                iconCode = 407;
                break;
            case "小到中雪":
                iconCode = 408;
                break;
            case "中到大雪":
                iconCode = 409;
                break;
            case "大到暴雪":
                iconCode = 410;
                break;
            case "雪":
                iconCode = 499;
                break;
            case "薄雾":
                iconCode = 500;
                break;
            case "雾":
                iconCode = 501;
                break;
            case "霾":
                iconCode = 502;
                break;
            case "扬沙":
                iconCode = 503;
                break;
            case "浮尘":
                iconCode = 504;
                break;
            case "沙尘暴":
                iconCode = 507;
                break;
            case "强沙尘暴":
                iconCode = 508;
                break;
            case "浓雾":
                iconCode = 509;
                break;
            case "强浓雾":
                iconCode = 510;
                break;
            case "中度霾":
                iconCode = 511;
                break;
            case "重度霾":
                iconCode = 512;
                break;
            case "严重霾":
                iconCode = 513;
                break;
            case "大雾":
                iconCode = 514;
                break;
            case "特强浓雾":
                iconCode = 515;
                break;
            case "热":
                iconCode = 900;
                break;
            case "冷":
                iconCode = 901;
                break;
            case "未知":
                iconCode = 999;
                break;

        }
        return iconCode;
    }
}
