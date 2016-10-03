package com.example.cyc.weatherinf.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cyc.weatherinf.R;
import com.example.cyc.weatherinf.adapter.ViewPageAdapter;
import com.example.cyc.weatherinf.fragment.WeatherFragment;
import com.example.cyc.weatherinf.utils.ActivityCollector;
import com.example.cyc.weatherinf.utils.CityIdSend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by cyc on 2016/9/6.
 */
public class MainActivity extends BaseCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static MainActivity weather;
    //notify
    private NotificationManager mNotificationManger;
    private int notify = 100;

    private Unbinder unbinder;
    private ViewPageAdapter viewPageAdapter;
    private String cityId;
    private CityIdSend send;
    private Set<String> cityIdAndName = new HashSet<>();//set无序。所有合一起写
    private SharedPreferences sharedPreferences;
    private List<WeatherFragment> fragments;

    private long exitTime = 0;


    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.main_content)
    CoordinatorLayout coordinatorLayout;

    //https://api.heweather.com/x3/weather?cityid=CN101310230&key=00a37aa472f34fcb8ae6c7a4f2de453a


    public MainActivity() {
        weather = this;
    }

    public static MainActivity getWeather() {
        return weather;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        ActivityCollector.addActivity(this);
        //城市写入数组
        initData();
        //设置notificationManger
        initNotify();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        judge();
        addFragment();

        mViewPager.setAdapter(viewPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(viewPageAdapter);

        fab.setOnClickListener(this);
    }

    private void initNotify() {
        mNotificationManger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void initData() {
        sharedPreferences = getSharedPreferences("WeatherData", 0);
        cityIdAndName = sharedPreferences.getStringSet("cityNameIdSet", cityIdAndName);
        Intent intent = getIntent();
        String s = intent.getStringExtra("cityNameId");
        if (s != null) {
            cityIdAndName.add(s);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("cityNameIdSet", cityIdAndName);
            editor.commit();
        }
    }


    public void judge() {
        //判断是否有一个或一个以上城市
        if (cityIdAndName.size() <= 0) {
            Snackbar.make(coordinatorLayout,"需要添加一个城市",Snackbar.LENGTH_LONG)
                    .setAction("Add", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
                            intent.putExtra("noCity", true);
                            startActivity(intent);
                        }
                    }).setDuration(Snackbar.LENGTH_LONG).show();
        }
    }

    public void showNotify(String cityName,String cityHum,String cityWeather) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, getIntent(), 0);
        mBuilder.setSmallIcon(R.mipmap.appicon)
                .setContentTitle("我的天气")
                .setContentText(cityName + "   " + cityHum + "   " + cityWeather)
                .setContentIntent(pendingIntent);
        Notification mNotification = mBuilder.build();
        mNotification.icon = R.mipmap.appicon;
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        mNotification.when = System.currentTimeMillis();
        mNotificationManger.notify(notify, mNotification);

    }

    public void setListener(CityIdSend cityIdSend) {
        send = cityIdSend;
        send.sendId(cityId);
    }

    private void addFragment() {
        fragments = new ArrayList<>();
        List<String> title = new ArrayList<>();

        for (String city : cityIdAndName) {
            String[] strings = city.split("#");
            cityId = strings[1];
            fragments.add(new WeatherFragment());
            title.add(strings[0]);
            mTabLayout.addTab(mTabLayout.newTab().setText(strings[0]));
        }
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), fragments, title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        this.finish();
    }

    @Override
    protected void setUpCommonBackToolBar(int toolbarId, String title) {
        super.setUpCommonBackToolBar(toolbarId, title);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.add_city:
                sharedPreferences.edit().putBoolean("addCity", true).commit();
                startActivity(new Intent(this, SelectCityActivity.class));
                break;
            case R.id.city_manger:
                startActivity(new Intent(this, CityMangerActivity.class));
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        //fab do something
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            ActivityCollector.finishAll();
        }
    }
}
