package com.tao.weather.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fanglin.fenhong.mapandlocate.baiduloc.BaiduLocateUtil;
import com.fanglin.fenhong.mapandlocate.baiduloc.FHLocation;
import com.tao.weather.R;
import com.tao.weather.activity.MainActivity;
import com.tao.weather.bean.WeatherBean;
import com.tao.weather.utils.Constants;
import com.tao.weather.utils.DisplayUtils;
import com.tao.weather.utils.QueryWeatherInfoUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/11/21.
 */

public class WeatherInfoFragment extends Fragment {
    private static final String TAG = "WeatherInfoFragment";
    private LinearLayout ll_container;
    private TextView tv_city_name;
    private TextView tv_error;
    private TextView tv_current_time;
    private TextView tv_update_time;
    private TextView tv_temperature;
    private TextView tv_desc;
    List<WeatherBean.HeWeather5Bean> heWeather5BeanList;
    public boolean hava_weather_info = true;
    private ImageView iv_weather_icon;
    private ImageButton ib_speech;
    private WeatherBean.HeWeather5Bean heWeather5Bean;
    private Handler mHandler;
    private Runnable mRunnable;
    private LinearLayout ll_loading;
    private String cityName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_weather_info, null);
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: ！！！");
                if (!MainActivity.mTts.isSpeaking()) {
                    ib_speech.setBackgroundResource(R.drawable.icon_nospeech);
                    mHandler.removeCallbacks(mRunnable);
                    return;
                }
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 1000);
            }
        };
        //获取传入fragment的数据
        Bundle args = getArguments();
        initView(view);
        initListener();
        if (args != null) { //如果数据不为空则按照数据中的城市名称查询天气信息
            Log.i(TAG, "onCreateView: hava cityName");
            cityName = args.getString("cityName");
            getCurrentCityWeatherInfo(cityName);
        } else {
            getCurrentCity();
        }
        return view;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void initView(View view) {
        ll_container = view.findViewById(R.id.ll_container);
        ll_loading = view.findViewById(R.id.ll_loading);
        tv_city_name = view.findViewById(R.id.tv_city_name);
        tv_error = view.findViewById(R.id.tv_error);
        tv_current_time = view.findViewById(R.id.tv_current_time);
        tv_update_time = view.findViewById(R.id.tv_update_time);
        tv_temperature = view.findViewById(R.id.tv_temperature);
        ib_speech = view.findViewById(R.id.ib_speech);
        tv_desc = view.findViewById(R.id.tv_desc);
        iv_weather_icon = view.findViewById(R.id.iv_weather_icon);
    }

    /**
     * 初始化按钮监听事件
     */
    private void initListener() {
        ib_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mTts.isSpeaking()) {
                    MainActivity.mTts.stopSpeaking();
                    ib_speech.setBackgroundResource(R.drawable.icon_nospeech);
                    mHandler.removeCallbacks(mRunnable);
                } else {
                    if (heWeather5Bean != null) {
                        ib_speech.setBackgroundResource(R.drawable.icon_speeching);
                        if (heWeather5Bean.getDaily_forecast() == null) {
                            MainActivity.mTts.startSpeaking(heWeather5Bean.getBasic().getCity() + "市,天气" +
                                    heWeather5Bean.getNow().getCond().getTxt() + ",温度"
                                    + heWeather5Bean.getNow().getTmp() + "℃", null);
                        } else {
                            MainActivity.mTts.startSpeaking(heWeather5Bean.getBasic().getCity() + "市,天气" +
                                    heWeather5Bean.getNow().getCond().getTxt() + ",最高温度"
                                    + heWeather5Bean.getDaily_forecast().get(0).getTmp().getMax() + "℃,最低温度"
                                    + heWeather5Bean.getDaily_forecast().get(0).getTmp().getMin() + "℃,当前温度"
                                    + heWeather5Bean.getNow().getTmp() + "℃,"
                                    + heWeather5Bean.getDaily_forecast().get(0).getWind().getDir() + ",风力"
                                    + heWeather5Bean.getDaily_forecast().get(0).getWind().getSc()
                                    + "级,相对湿度" + heWeather5Bean.getDaily_forecast().get(0).getHum() + "%", null);
                        }
                        mHandler.post(mRunnable);
                    }
                }
            }
        });
    }

    /**
     * 初始化数据
     *
     * @param haveInfo true为有数据的情况，显示天气信息  false则没能成功获取数据，不显示天气信息，显示数据错误的提示信息
     */
    private void initData(boolean haveInfo) {
        ll_loading.setVisibility(View.INVISIBLE);////设置加载天气信息布局不可见
        if (haveInfo) {
            tv_error.setVisibility(View.INVISIBLE);//设置错误文本不可见
            ll_container.setVisibility(View.VISIBLE);//设置天气信息布局可见
            if (heWeather5BeanList != null) {
                heWeather5Bean = heWeather5BeanList.get(0);
                tv_city_name.setText(heWeather5Bean.getBasic().getCity());
                tv_current_time.setText(QueryWeatherInfoUtil.getStringData());
                String update_time = heWeather5Bean.getBasic().getUpdate().getLoc();
                update_time = update_time.substring(update_time.length() - 5, update_time.length());
                tv_update_time.setText(update_time + " 更新");
                ib_speech.setBackgroundResource(R.drawable.icon_nospeech);
                ib_speech.setVisibility(View.VISIBLE);
                tv_temperature.setText(heWeather5Bean.getNow().getTmp() + "℃");
                tv_desc.setText(heWeather5Bean.getNow().getCond().getTxt());
                int iconCode = DisplayUtils.getWeatherIcon(heWeather5Bean.getNow().getCond().getTxt());
                if (iconCode != -1) {
                    Glide.with(getActivity()).load(Constants.ICON_URL + iconCode + ".png").into(iv_weather_icon);
                }
                Log.i(TAG, "initData: " + Constants.ICON_URL + iconCode + ".png");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, DisplayUtils.dp2px(getActivity(), 12));
                for (int i = 0; i < 8; i++) {
                    View itemView = View.inflate(getActivity(), R.layout.item_weather_suggestion, null);
                    itemView.setLayoutParams(lp);
                    TextView tv_key = itemView.findViewById(R.id.tv_key);
                    TextView tv_txt = itemView.findViewById(R.id.tv_txt);
                    switch (i) {
                        case 0:
                            if (heWeather5Bean.getSuggestion() != null) {
                                tv_key.setText("空气质量：" + heWeather5Bean.getSuggestion().getAir().getBrf());
                                tv_txt.setText(heWeather5Bean.getSuggestion().getAir().getTxt());
                            }
                            break;
                        case 1:
                            if (heWeather5Bean.getSuggestion() != null) {
                                tv_key.setText("舒适指数：" + heWeather5Bean.getSuggestion().getComf().getBrf());
                                tv_txt.setText(heWeather5Bean.getSuggestion().getComf().getTxt());
                            }
                            break;
                        case 2:
                            if (heWeather5Bean.getSuggestion() != null) {
                                tv_key.setText("洗车指数：" + heWeather5Bean.getSuggestion().getCw().getBrf());
                                tv_txt.setText(heWeather5Bean.getSuggestion().getCw().getTxt());
                            }
                            break;
                        case 3:
                            if (heWeather5Bean.getSuggestion() != null) {
                                tv_key.setText("穿衣指数：" + heWeather5Bean.getSuggestion().getDrsg().getBrf());
                                tv_txt.setText(heWeather5Bean.getSuggestion().getDrsg().getTxt());
                            }
                            break;
                        case 4:
                            if (heWeather5Bean.getSuggestion() != null) {
                                tv_key.setText("感冒指数：" + heWeather5Bean.getSuggestion().getFlu().getBrf());
                                tv_txt.setText(heWeather5Bean.getSuggestion().getFlu().getTxt());
                            }
                            break;
                        case 5:
                            if (heWeather5Bean.getSuggestion() != null) {
                                tv_key.setText("运动指数：" + heWeather5Bean.getSuggestion().getSport().getBrf());
                                tv_txt.setText(heWeather5Bean.getSuggestion().getSport().getTxt());
                            }
                            break;
                        case 6:
                            if (heWeather5Bean.getSuggestion() != null) {
                                tv_key.setText("旅游指数：" + heWeather5Bean.getSuggestion().getTrav().getBrf());
                                tv_txt.setText(heWeather5Bean.getSuggestion().getTrav().getTxt());
                            }
                            break;
                        case 7:
                            if (heWeather5Bean.getSuggestion() != null) {
                                tv_key.setText("紫外线指数：" + heWeather5Bean.getSuggestion().getUv().getBrf());
                                tv_txt.setText(heWeather5Bean.getSuggestion().getUv().getTxt());
                            }
                            break;
                    }
                    //如果建议内容不为空
                    if (!tv_key.getText().toString().equals("")) {
                        ll_container.addView(itemView);
                    }
                }
            }
        } else {
            tv_error.setVisibility(View.VISIBLE);//设置错误文本可见
            ll_container.setVisibility(View.INVISIBLE);//设置天气信息布局不可见
        }
    }


    /**
     * 定位当前城市
     */
    public void getCurrentCity() {
        ll_loading.setVisibility(View.VISIBLE);////设置加载天气信息布局不可见
        BaiduLocateUtil.getinstance(getActivity()).start();
        BaiduLocateUtil.getinstance(getActivity()).setCallBack(new BaiduLocateUtil.LocationCallBack() {
            @Override
            public void onChange(FHLocation location) {
                if (location != null) {
                    BaiduLocateUtil.getinstance(getActivity()).stop();
                    Log.i(TAG, "onChange: " + location.toString());
                    getCurrentCityWeatherInfo(location.city);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: " + "定位失败");
                initData(false);
                hava_weather_info = false;
            }
        });
    }

    /**
     * 获取当前城市的天气信息
     *
     * @param cityName
     */
    private void getCurrentCityWeatherInfo(String cityName) {
        OkHttpUtils
                .get()
                .url(Constants.WEATHER_URL)
                .addParams("city", cityName)
                .addParams("key", Constants.KEY)
                .build()
                .execute(new StringCallback() {
                    /**
                     * 当请求失败的时候回调
                     *
                     * @param call
                     * @param e
                     * @param id
                     */
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i(TAG, "onError: " + e.getMessage());
                        initData(false);
                        hava_weather_info = false;
                        Toast.makeText(getActivity(), "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * 当联网成功的时候回调
                     *
                     * @param response 请求成功的数据
                     * @param id
                     */
                    @Override
                    public void onResponse(String response, int id) {
                        Log.i(TAG, "onResponse: " + response);
                        heWeather5BeanList = QueryWeatherInfoUtil.processData(response);
                        if (!heWeather5BeanList.get(0).getStatus().equals("ok")){
                            initData(false);
                        }else{
                            initData(true);
                        }
                        hava_weather_info = true;
                    }
                });
    }

    @Override
    public void onDestroy() {
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        if (MainActivity.mTts.isSpeaking()) {
            MainActivity.mTts.stopSpeaking();
        }
        super.onDestroy();
    }
}
