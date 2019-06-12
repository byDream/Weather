package com.tao.weather.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.tao.weather.R;
import com.tao.weather.fragment.CitySelectFragment;
import com.tao.weather.fragment.WeatherInfoFragment;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int position = 0;
    private WeatherInfoFragment weatherInfoFragment;
    private CitySelectFragment citySelectFragment;
    private RadioGroup rg_main;
    private long mPreTime;//上一次按返回键的时间
    //缓存的Fragment或者上次显示的Fragment
    private Fragment tempFragment;
    public static SpeechSynthesizer mTts;

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
        setContentView(R.layout.activity_main);
        initView();
        initListener();

        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5bf8bc37");

        initSpeechSynthesizer();
        //默认设置为天气信息页面
        rg_main.check(R.id.rb_weather);
    }

    private void initSpeechSynthesizer() {
        mTts = SpeechSynthesizer.createSynthesizer(this, null);

        /**
         2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
         *
         */

        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        boolean isSuccess = mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts2.wav");
//        Toast.makeText(MainActivity.this, "语音合成 保存音频到本地：\n" + isSuccess, Toast.LENGTH_LONG).show();
        //3.开始合成
//        mTts.startSpeaking("在这里放置需要进行合成的文本", null);
    }


    private void initView() {
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        weatherInfoFragment = new WeatherInfoFragment();
        citySelectFragment = new CitySelectFragment();

    }

    private void initListener() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_weather://天气信息
                        position = 0;
                        break;
                    case R.id.rb_cityselect://选择城市
                        position = 1;
                        break;
                    default:
                        position = 0;
                        break;
                }
                //根据位置取不同的Fragment
                if (position == 0) {
                    switchFragment(tempFragment, weatherInfoFragment);
                    if (!weatherInfoFragment.hava_weather_info) {//如果天气信息异常则重新请求数据
                        Log.i(TAG, "onCheckedChanged: " + "require_weather_info");
                        weatherInfoFragment.getCurrentCity();
                    }
                } else {
                    switchFragment(tempFragment, citySelectFragment);
                }

            }
        });

    }

    /**
     * 切换Fragment
     *
     * @param fromFragment
     * @param nextFragment
     */
    private void switchFragment(Fragment fromFragment, Fragment nextFragment) {
        if (tempFragment != nextFragment) {
            tempFragment = nextFragment;
            if (nextFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //判断nextFragment是否添加
                if (!nextFragment.isAdded()) {
                    //隐藏当前Fragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    //添加Fragment
                    transaction.add(R.id.fl_fragment, nextFragment).commit();
                } else {
                    //隐藏当前Fragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.show(nextFragment).commit();
                }

            }
        }
    }

    /**
     * 退出控制
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mPreTime > 2000) {// 两次点击间隔大于2s
            Toast.makeText(this, "再按一次退出和风天气", Toast.LENGTH_SHORT).show();
            mPreTime = System.currentTimeMillis();
        } else {
            if (Build.VERSION.SDK_INT > 23) {//判断设备android版本是否>android6.0
                System.exit(0);
            } else {
                finish();
            }

        }
    }

    @Override
    protected void onDestroy() {
        if (mTts != null) {
            mTts.destroy();
        }
        super.onDestroy();
    }
}
