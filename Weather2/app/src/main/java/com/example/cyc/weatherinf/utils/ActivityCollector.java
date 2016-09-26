package com.example.cyc.weatherinf.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyc on 2016/9/23.
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void finishAll() {
        for(int i = activities.size()-1;i>=0;i--) {
            if (!activities.get(i).isFinishing()) {
                activities.get(i).finish();
            }
        }
    }
}