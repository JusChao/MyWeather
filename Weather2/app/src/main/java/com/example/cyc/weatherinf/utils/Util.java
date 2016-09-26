package com.example.cyc.weatherinf.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.cyc.weatherinf.R;
import com.github.promeg.pinyinhelper.Pinyin;

/**
 * Created by cyc on 2016/9/6.
 */
public class Util {
    public static int getToolBarHeight(Context context) {
        final TypedArray styleAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize}
        );
        int toolBarHeight = (int) styleAttributes.getDimension(0, 0);
        styleAttributes.recycle();

        return toolBarHeight;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String transformPinYin(String cityName) {
        StringBuffer cityPinYin = new StringBuffer();
        for(int i =0 ;i<cityName.length();i++) {
            cityPinYin.append(Pinyin.toPinyin(cityName.charAt(i)));
        }
        return cityPinYin.toString();
    }
}