package com.example.cyc.weatherinf.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cyc.weatherinf.R;

/**
 * Created by cyc on 2016/9/23.
 */
public class CityMangerViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;
    public ImageView mainCityView;
    public LinearLayout linearLayout;
    public RelativeLayout relativeLayout;

    public CityMangerViewHolder(View itemView) {
        super(itemView);
//        relativeLayout = (RelativeLayout) itemView.findViewById(R.id.city_manger_rl);
        textView = (TextView) itemView.findViewById(R.id.item_manger_txt);
//        imageView = (ImageView) itemView.findViewById(R.id.item_manger_delete_img);
        mainCityView = (ImageView) itemView.findViewById(R.id.main_city_icon);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.item_manger_ll);
    }
}
