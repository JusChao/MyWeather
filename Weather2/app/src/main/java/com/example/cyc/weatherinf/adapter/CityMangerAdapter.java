package com.example.cyc.weatherinf.adapter;

/**
 * Created by cyc on 2016/9/25.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cyc.weatherinf.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CityMangerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private SharedPreferences sharedPreferences;
    private List<String> list = new ArrayList<>();
    private Set<String> set = new HashSet<>();

    private OnClickListener listener;

    public interface OnClickListener {
        void onClick(String cityName);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public CityMangerAdapter(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences("WeatherData", 0);
        set = sharedPreferences.getStringSet("cityNameIdSet", set);
        list.addAll(set);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityMangerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.city_manger_item, null, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CityMangerViewHolder viewHolder = (CityMangerViewHolder) holder;
        viewHolder.textView.setText(list.get(position));
        viewHolder.linearLayout.scrollTo(0, 0);
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(position));
            }
        });
        //#00   默认没有主城市
        if (sharedPreferences.getString("main_city", "#00").equals(list.get(position))) {
            viewHolder.mainCityView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mainCityView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void removeItem(int position) {
        if (list.size() < 2) {
            Toast.makeText(mContext, "需要保留一个城市", Toast.LENGTH_SHORT).show();
        }

        set.remove(list.remove(position));
        notifyDataSetChanged();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("cityNameIdSet", set);
        editor.commit();

    }
}