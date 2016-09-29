package com.example.cyc.weatherinf.view;

import com.example.cyc.weatherinf.module.CityWeather;

/**
 * Created by cyc on 2016/9/7.
 */
public interface ShowWeather {
    void sendWeather(CityWeather cityWeather);

    void stopRefresh();

}