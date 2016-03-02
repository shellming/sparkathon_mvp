package com.shellming.sparkathon_mvp.presenters.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shellming.sparkathon_mvp.R;
import com.shellming.sparkathon_mvp.models.Weather;
import com.shellming.sparkathon_mvp.views.ViewHolders.TendaysForcastViewHolder;

import java.util.List;

/**
 * Created by ruluo1992 on 12/11/2015.
 */
public class TendayViewAdaper extends RecyclerView.Adapter<TendaysForcastViewHolder>{
    private List<Weather> data;
    private Context context;

    public TendayViewAdaper(List<Weather> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public TendaysForcastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_card, parent, false);
        return new TendaysForcastViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(TendaysForcastViewHolder holder, int position) {
        holder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Weather> data){
        this.data = data;
        notifyDataSetChanged();
    }
}
