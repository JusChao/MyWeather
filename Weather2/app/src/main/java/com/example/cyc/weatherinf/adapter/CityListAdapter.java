package com.example.cyc.weatherinf.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cyc.weatherinf.R;
import com.example.cyc.weatherinf.module.CityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyc on 2016/9/20.
 */
public class CityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<CityBean> cityBeanList = new ArrayList<>();

    private OnClickListener listener;

    public CityListAdapter(Context mContext, List<CityBean> cityBeanList) {
        this.mContext = mContext;
        this.cityBeanList = cityBeanList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.search_city_item, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CityViewHolder) {
            CityViewHolder viewHolder = (CityViewHolder) holder;
            CityBean cityBean = cityBeanList.get(position);
            viewHolder.cityNameText.setText(cityBean.cityChinese);

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(position);
                }
            });

            if (position == 0) {
                viewHolder.cityNameHeader.setVisibility(View.VISIBLE);
                viewHolder.cityNameHeader.setText(String.valueOf(cityBean.cityPinYin.charAt(0)));
            } else {
                if (!TextUtils.equals(String.valueOf(cityBean.cityPinYin.charAt(0))
                        , String.valueOf(cityBeanList.get(position - 1).cityPinYin.charAt(0)))) {

                    viewHolder.cityNameHeader.setVisibility(View.VISIBLE);
                    viewHolder.cityNameHeader.setText(String.valueOf(cityBean.cityPinYin.charAt(0)));
                } else {
                    viewHolder.cityNameHeader.setVisibility(View.GONE);
                }
            }
            viewHolder.itemView.setContentDescription(String.valueOf(cityBean.cityPinYin.charAt(0)));
        }
    }

    @Override
    public int getItemCount() {
        return cityBeanList.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {
        TextView cityNameHeader, cityNameText;
        CardView cardView;
        public CityViewHolder(View itemView) {
            super(itemView);
            cityNameHeader = (TextView) itemView.findViewById(R.id.tv_sticky_header_view);
            cityNameText = (TextView) itemView.findViewById(R.id.tv_name);
            cardView = (CardView) itemView.findViewById(R.id.cardview_item);
        }
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }
}
