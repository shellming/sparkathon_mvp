package com.shellming.sparkathon_mvp.presenters.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.shellming.sparkathon_mvp.R;
import com.shellming.sparkathon_mvp.models.Weather;
import com.shellming.sparkathon_mvp.views.ViewHolders.CurrentObViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruluo1992 on 11/3/2015.
 */
public class CurrentObAdapter extends RecyclerView.Adapter<CurrentObViewHolder> {
    private List<Map> data;

    private int lastPosition = -1;

    public CurrentObAdapter(){
        this.data = new ArrayList<>();
    }

    public void setData(Weather weather){
        this.data = fromWeather(weather);
        notifyDataSetChanged();
    }

    private List<Map> fromWeather(Weather weather){
        List<Map> data = new ArrayList<>();
        Map huminity = new HashMap();
        huminity.put("icon", R.drawable.icon_wet);
        huminity.put("content", weather.getRh() + "%");

        Map windSpeed = new HashMap();
        windSpeed.put("icon", R.drawable.icon_windspeed);
        windSpeed.put("content", weather.getWspd() + " km/h");

        Map visit = new HashMap();
        visit.put("icon", R.drawable.icon_visit);
        visit.put("content", weather.getVis().toString());

        Map sunrise = new HashMap();
        sunrise.put("icon", R.drawable.icon_sunrise);
        String sunriseStr = weather.getSunrise();
        int start = sunriseStr.indexOf('T');
        int end = sunriseStr.indexOf('+');
        sunriseStr = sunriseStr.substring(start + 1, end);
        sunrise.put("content", sunriseStr);

        Map sunset = new HashMap();
        sunset.put("icon", R.drawable.icon_sunset);
        String sunsetStr = weather.getSunset();
        sunsetStr = sunsetStr.substring(start + 1, end);
        sunset.put("content", sunsetStr);

        Map pressure = new HashMap();
        pressure.put("icon", R.drawable.icon_pressure);
        pressure.put("content", weather.getMslp() + " mbar");

        data.add(huminity);
        data.add(windSpeed);
        data.add(visit);
        data.add(sunrise);
        data.add(sunset);
        data.add(pressure);

        return data;
    }

    @Override
    public CurrentObViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_detail, parent, false);
        return new CurrentObViewHolder(view, data);
    }

    @Override
    public void onBindViewHolder(CurrentObViewHolder holder, int position) {
        holder.setContent(position);
        setAnimation(holder.mView, position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.fade_in);
        animation.setStartOffset(100 * position);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
