package com.example.cyc.weatherinf.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.cyc.weatherinf.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cyc on 2016/9/24.
 */
public class GuideActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                GuideActivity.this.finish();
            }
        };
        timer.schedule(timerTask, 1000 * 2);
    }

}