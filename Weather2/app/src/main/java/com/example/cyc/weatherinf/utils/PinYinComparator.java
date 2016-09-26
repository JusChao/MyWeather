package com.example.cyc.weatherinf.utils;

import com.example.cyc.weatherinf.module.CityBean;

import java.util.Comparator;

/**
 * Created by cyc on 2016/9/20.
 */

//根据拼音对城市进行排序
public class PinYinComparator implements Comparator<CityBean> {

    @Override
    public int compare(CityBean lhs, CityBean rhs) {
        return lhs.cityPinYin.compareTo(rhs.cityPinYin);
    }
}