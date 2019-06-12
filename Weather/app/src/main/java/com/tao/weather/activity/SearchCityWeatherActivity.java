package com.tao.weather.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.tao.weather.R;
import com.tao.weather.fragment.WeatherInfoFragment;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;

public class SearchCityWeatherActivity extends AppCompatActivity {
    private static final String TAG = "SearchCityWeatherActivity";
    private LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        //改变状态栏的颜色
        if (Build.VERSION.SDK_INT >= 23) {//判断设备android版本是否>=android6.0
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(Color.parseColor("#055674"));
            //修改系统状态栏的字体的颜色 SYSTEM_UI_FLAG_LIGHT_STATUS_BAR:黑色,SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR:白色
            this.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        setContentView(R.layout.activity_search_city_weather);
        initView();
        initListener();
        //获取传递过来的数据
        String cityName = getIntent().getStringExtra("cityName");
        //添加WeatherInfoFragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        WeatherInfoFragment weatherInfoFragment = new WeatherInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cityName", cityName);
        weatherInfoFragment.setArguments(bundle);
        transaction.add(R.id.fl_fragment, weatherInfoFragment);
        transaction.commit();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
    }

    /**
     * 初始化按钮监听事件
     */
    private void initListener() {
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
