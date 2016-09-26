package com.example.cyc.weatherinf.retrofit;

import com.example.cyc.weatherinf.module.City;
import com.example.cyc.weatherinf.module.CityWeather;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by cyc on 2016/9/7.
 */
public interface WeatherService {
    @GET
    Observable<CityWeather> getWeather(@Url String weatherUrl);

    @GET
    Observable<City> getCity(@Url String cityUrl);
}