package com.tao.weather.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.params.ButtonParams;
import com.tao.weather.R;

public class SplashActivity extends AppCompatActivity {
    public long mTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        //去掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);



        if (Build.VERSION.SDK_INT >= 23) {//判断当前设备android版本是否>=android6.0
            //检查是否拥有权限
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {//1500毫秒后进入MusicActivity页面
                        enterMainAactivity();
                    }
                }, 1500);
            } else {
                applyPermission();
            }
        } else {//1500毫秒后进入MusicActivity页面
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterMainAactivity();
                }
            }, 1500);
        }
    }

    /**
     * 申请权限
     */
    public void applyPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //申请权限
            int i = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);//定位权限
            if (i != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                mTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * 跳转至MainAactivity页面
     */
    private void enterMainAactivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * 权限申请结束后的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @SuppressLint("WrongConstant")
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //如果权限申请成功,判断是否超过1500毫秒,没有超过也要等到1500毫秒到了才能进入页面
                long time = 1500 - System.currentTimeMillis() - mTime;
                if (time > 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enterMainAactivity();
                        }
                    }, time);
                } else {
                    enterMainAactivity();
                }
            } else {
                showDialog();
            }
        }
    }

    /**
     * 显示权限申请对话框
     */
    private void showDialog() {
        new CircleDialog.Builder(this)
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)//不能取消,除了点击对话框自身的取消按钮
                .setTitle("获取权限")
                .setTitleColor(Color.parseColor("#2981e5"))
                .setText("没有授予权限将无法获取天气信息,是否重新授予权限")
                .setNegative("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showDialog();
                            }
                        }, 1000);
                    }
                })
                .setPositive("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        applyPermission();
                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.RED;
                    }
                })
                .show();
    }
}
