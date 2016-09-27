package com.example.cyc.weatherinf.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.cyc.weatherinf.R;
import com.example.cyc.weatherinf.adapter.CityMangerAdapter;

/**
 * Created by cyc on 2016/9/23.
 */
public class CityMangerActivity extends BaseCompatActivity {

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
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
        super.onBackPressed();
    }
}