package com.shellming.sparkathon_mvp.views;

import com.shellming.sparkathon_mvp.models.Weather;

import java.util.List;

/**
 * Created by ruluo1992 on 2/28/2016.
 */
public interface IOneDayForcastView extends IView{
    public void setData(List<Weather> weathers);
    public void startRefresh();
    public void stopRefresh();
}
