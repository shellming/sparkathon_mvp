package com.shellming.sparkathon_mvp.views;

import android.support.v4.widget.SwipeRefreshLayout;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.shellming.sparkathon_mvp.models.Weather;

/**
 * Created by ruluo1992 on 2/21/2016.
 */
public interface ICurrentObView extends IView {
    public void startRefresh();
    public void stopRefresh();
    public FloatingActionsMenu getFloatingActionMenu();
    public void setData(Weather weather);
}
