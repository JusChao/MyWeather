package com.example.cyc.weatherinf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cyc.weatherinf.R;
import com.example.cyc.weatherinf.adapter.CityListAdapter;
import com.example.cyc.weatherinf.module.CityBean;
import com.example.cyc.weatherinf.retrofit.HttpMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by cyc on 2016/9/7.
 */
public class SelectCityActivity extends BaseCompatActivity implements GetCityList, CityListAdapter.OnClickListener {

    private Unbinder unbinder;

    @BindView(R.id.tv_sticky_header_view)
    TextView headerTextView;
    @BindView(R.id.my_recycler)
    RecyclerView cityListView;
    @BindView(R.id.search_edit)
    EditText searchEditText;
    @BindView(R.id.select_toolbar)
    Toolbar mToolbar;

    private List<CityBean> cityBeans = new ArrayList<>();
    private List<CityBean> cityBeanCopy = new ArrayList<>();
    private CityListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean noCity = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        noCity = getIntent().getBooleanExtra("noCity", false);
        initRecyler();
        searchEditText.setVisibility(View.GONE);

        HttpMethods.getInstance("city").getCity(this);//""选择城市不用cityId

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void editListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!"".equals(s.toString())) {
                    List<CityBean> cityBeanList = new ArrayList<CityBean>();
                    for (CityBean cb : cityBeanCopy) {
                        if (cb.cityPinYin.contains(String.valueOf(s).toUpperCase()) || cb.cityChinese.contains(s)) {
                            cityBeanList.add(cb);
                        }
                    }
                    if (cityBeanList.size() > 0) {
                        refreshData(cityBeanList);
                    }
                } else {
                    refreshData(cityBeanCopy);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRecyler() {
        adapter = new CityListAdapter(this, cityBeans);
        layoutManager = new LinearLayoutManager(this);
        cityListView.setLayoutManager(layoutManager);
        cityListView.setHasFixedSize(true);
        cityListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = cityListView.findChildViewUnder(10, 5);
                if (view != null && view.getContentDescription() != null) {
                    headerTextView.setText(String.valueOf(view.getContentDescription()));
                }
            }
        });
    }

    private void refreshData(List<CityBean> cityBeanList) {
        cityBeans.clear();
        for (CityBean cb : cityBeanList) {
            cityBeans.add(cb);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }

    @Override
    public void getList(List<CityBean> cityBeen) {
        this.cityBeans = cityBeen;
        for (CityBean cb : cityBeen) {
            cityBeanCopy.add(cb);
        }
        adapter = new CityListAdapter(this, cityBeen);
        adapter.setOnClickListener(this);
        cityListView.setAdapter(adapter);
        searchEditText.setVisibility(View.VISIBLE);
        editListener();
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, MainActivity.class);
        String nameAndId = cityBeans.get(position).cityChinese + "#" + cityBeans.get(position).cityId;
        intent.putExtra("cityNameId", nameAndId);
        if (noCity) {
            getSharedPreferences("WeatherData", 0).edit().putString("main_city", nameAndId).commit();
        }
        startActivity(intent);
        this.finish();
    }
}