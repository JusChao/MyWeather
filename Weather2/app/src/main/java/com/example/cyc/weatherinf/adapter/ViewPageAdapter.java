package com.example.cyc.weatherinf.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.cyc.weatherinf.fragment.WeatherFragment;

import java.util.List;

/**
 * Created by cyc on 2016/9/8.
 */
public class ViewPageAdapter extends FragmentStatePagerAdapter{
    private List<WeatherFragment> fragments;
    private List<String> strings;

    public ViewPageAdapter(FragmentManager fm, List<WeatherFragment> fragments, List<String> strings) {
        super(fm);
        this.fragments = fragments;
        this.strings = strings;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strings.get(position);
    }
}