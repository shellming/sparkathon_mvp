package com.shellming.sparkathon_mvp.presenters;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by ruluo1992 on 2/21/2016.
 */
public interface ICurrentObPresenter extends IPresenter {
    public void refreshData();
    public void clickYes();
    public void clickNo();
}
