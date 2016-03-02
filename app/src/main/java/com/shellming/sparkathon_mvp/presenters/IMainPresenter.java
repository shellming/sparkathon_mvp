package com.shellming.sparkathon_mvp.presenters;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by ruluo1992 on 2/21/2016.
 */
public interface IMainPresenter extends IPresenter {
    public void clickAvatar();
    public void userLogin(String oauthVerifier);
}
