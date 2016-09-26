package com.example.cyc.weatherinf.fragment;

import android.content.Context;
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

    public WeatherFragment() {
        MainActivity.getWeather().setLintener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment, container, false);
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

    @Override
    public void showWeather(CityWeather weather) {
        adapter = new WeatherAdapter(getContext(), weather);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void stopRefresh() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
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