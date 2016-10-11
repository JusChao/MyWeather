package com.example.cyc.weatherinf.retrofit;

import com.example.cyc.weatherinf.BuildConfig;
import com.example.cyc.weatherinf.base.BaseApplication;
import com.example.cyc.weatherinf.module.City;
import com.example.cyc.weatherinf.module.CityBean;
import com.example.cyc.weatherinf.module.CityWeather;
import com.example.cyc.weatherinf.utils.PinYinComparator;
import com.example.cyc.weatherinf.utils.Util;
import com.example.cyc.weatherinf.activity.GetCityList;
import com.example.cyc.weatherinf.activity.ShowWeather;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by cyc on 2016/9/7.
 */
public class HttpMethods {
    private static String URL = "citylist?search=allchina&key=00a37aa472f34fcb8ae6c7a4f2de453a";
    private final static String BASE_URL = "https://api.heweather.com/x3/";
    private final static String CITY_URL1 = "weather?cityid=";
    private final static String CITY_URL2 = "&key=00a37aa472f34fcb8ae6c7a4f2de453a";
    private static String weatherUrl;


    private Retrofit retrofit;
    private WeatherService service;
    private OkHttpClient client;

    private HttpMethods() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }

        Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();
                if (!Util.isNetworkConnected(BaseApplication.getmAppContext())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response originalResponse = chain.proceed(request);
                if (Util.isNetworkConnected(BaseApplication.getmAppContext())) {
                    int maxAge = 60;
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28;
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
//        OkHttpClient client = new OkHttpClient();
//        client.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        //setup cache
        File httpCacheDirectory = new File(BaseApplication.getCache(), "responses");
        int cacheSize = 100 * 1024 * 1024;
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        //add cache to the client
        builder.cache(cache).addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
/*
              retrofit2.adapter.rxjava.HttpException: HTTP 504 Unsatisfiable Request (only-if-cached)
              第一次使用时正常，后来出现这个问题，无法缓存
 */
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //fuck***
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(WeatherService.class);
    }

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance(String mId) {
        weatherUrl = CITY_URL1 + mId + CITY_URL2;
        return SingletonHolder.INSTANCE;
    }

    public void getCity(final GetCityList gcl) {
        service.getCity(URL)
                .subscribeOn(Schedulers.io())
                .map(new Func1<City, List<CityBean>>() {
                    @Override
                    public List<CityBean> call(City city) {
                        List<CityBean> cityBeans = new ArrayList<CityBean>();
                        for (City.CityInfoBean cityInfoBean : city.getCity_info()) {
                            CityBean cityBean = new CityBean();
                            cityBean.cityPinYin = Util.transformPinYin(cityInfoBean.getCity());
                            cityBean.cityChinese = cityInfoBean.getCity();
                            cityBean.cityId = cityInfoBean.getId();
                            cityBeans.add(cityBean);
                        }

                        Collections.sort(cityBeans, new PinYinComparator());
                        return cityBeans;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CityBean>>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<CityBean> cityBeen) {
                        gcl.getList(cityBeen);
                    }
                });
    }

    public void getWeather(final ShowWeather show) {
        service.getWeather(weatherUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CityWeather>() {
                    @Override
                    public void onCompleted() {
                        show.stopRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CityWeather cityWeather) {
                        show.sendWeather(cityWeather);
                    }
                });

    }
}