package com.example.cyc.weatherinf.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cyc.weatherinf.R;
import com.example.cyc.weatherinf.adapter.WeatherAdapter;
import com.example.cyc.weatherinf.module.CityWeather;
import com.example.cyc.weatherinf.retrofit.HttpMethods;
import com.example.cyc.weatherinf.utils.CityIdSend;
import com.example.cyc.weatherinf.view.MainActivity;
import com.example.cyc.weatherinf.view.ShowWeather;

/**
 * Created by cyc on 2016/9/7.
 */
public class WeatherFragment extends Fragment implements ShowWeather, CityIdSend, SwipeRefreshLayout.OnRefreshListener {
    private CityWeather weather;
    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private String cityId;
    private SwipeRefreshLayout refreshLayout;
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public WeatherFragment() {
        MainActivity.getWeather().setLintener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment, container, false);
        mContext = container.getContext();
        sharedPreferences = mContext.getSharedPreferences("WeatherData", 0);
        recyclerView = (RecyclerView) view.findViewById(R.id.weather_frag);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_fresh);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout.setColorSchemeColors(R.color.refreshColor1,R.color.refreshColor2);
        refreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        HttpMethods.getInstance(cityId).getWeather(this);
    }

    public void showWeather() {
//
//            sharedPreferences.edit().putString("main_city_hum", weather.getHeWeather().get(0).getNow().getTmp()).commit();
//            sharedPreferences.edit().putString("main_city_weather", weather.getHeWeather().get(0).getNow().getCond().getTxt()).commit();
//        }
        adapter = new WeatherAdapter(getContext(), weather);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void sendWeather(CityWeather cityWeather) {
        this.weather = cityWeather;
        showWeather();
        if (cityId.equals(sharedPreferences.getString("main_city", "#00").split("#")[1])) {
            showNotify();
        }
    }

    @Override
    public void stopRefresh() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    public void showNotify() {
        String cityName = weather.getHeWeather().get(0).getBasic().getCity();
        String cityHum = weather.getHeWeather().get(0).getNow().getTmp()+"℃";
        String cityWeahter = weather.getHeWeather().get(0).getNow().getCond().getTxt();
        MainActivity.getWeather().showNotify(cityName, cityHum, cityWeahter);
    }

    @Override
    public void sendId(String cityId) {
        this.cityId = cityId;
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        HttpMethods.getInstance(cityId).getWeather(this);
    }
}