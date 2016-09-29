package com.example.cyc.weatherinf.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.cyc.weatherinf.R;
import com.example.cyc.weatherinf.adapter.CityMangerAdapter;

/**
 * Created by cyc on 2016/9/23.
 */
public class CityMangerActivity extends BaseCompatActivity implements CityMangerAdapter.OnClickListener {

    private CityMangerRecylcerView cmrv;
    private CityMangerAdapter adapter;
    private LinearLayoutManager manager;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_manger_main);
        initView();
    }

    private void initView() {
        cmrv = (CityMangerRecylcerView) findViewById(R.id.city_manger_recycler);
        mToolbar = (Toolbar) findViewById(R.id.city_manger_toolbar);
        adapter = new CityMangerAdapter(getApplicationContext());
        adapter.setOnClickListener(this);
        manager = new LinearLayoutManager(getApplicationContext());
        cmrv.setLayoutManager(manager);
        cmrv.setAdapter(adapter);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(String cityName) {
        SharedPreferences.Editor editor = getSharedPreferences("WeatherData", 0).edit();
        editor.putString("main_city", cityName);
        editor.commit();
        adapter.notifyDataSetChanged();
    }
}