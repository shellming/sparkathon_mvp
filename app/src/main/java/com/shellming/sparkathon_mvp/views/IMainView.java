package com.shellming.sparkathon_mvp.views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.shellming.sparkathon_mvp.models.User;

/**
 * Created by ruluo1992 on 2/18/2016.
 */
public interface IMainView extends IView{
    public void setCityName(String name);
    public void setData(User user);
    public void clearData();
}
