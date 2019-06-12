package com.tao.weather.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tao.weather.R;
import com.tao.weather.activity.SearchCityWeatherActivity;

/**
 * Created by Administrator on 2018/11/21.
 */

public class CitySelectFragment extends Fragment {

    private EditText et_cityName;
    private Button btn_search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_city_select, null);
        initView(view);
        initListener();
        return view;
    }

    private void initView(View view) {
        et_cityName = view.findViewById(R.id.et_cityName);
        btn_search = view.findViewById(R.id.btn_search);
    }

    private void initListener() {
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = et_cityName.getText().toString().trim();
                if (cityName.equals("")) {
                    Toast.makeText(getActivity(), "请输入城市名称", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), SearchCityWeatherActivity.class);
                    intent.putExtra("cityName", cityName);
                    getActivity().startActivity(intent);
                }
            }
        });
    }
}
