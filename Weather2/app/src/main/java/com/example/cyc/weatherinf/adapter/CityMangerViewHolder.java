package com.example.cyc.weatherinf.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cyc.weatherinf.R;

/**
 * Created by cyc on 2016/9/23.
 */
public class CityMangerViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;
    public ImageView imageView;
    public LinearLayout linearLayout;

    public CityMangerViewHolder(View itemView) {
        super(itemView);

        textView = (TextView) itemView.findViewById(R.id.item_manger_txt);
        imageView = (ImageView) itemView.findViewById(R.id.item_manger_delete_img);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.item_manger_ll);
    }
}
