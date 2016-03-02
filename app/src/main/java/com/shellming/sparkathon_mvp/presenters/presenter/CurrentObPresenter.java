package com.shellming.sparkathon_mvp.presenters.presenter;

import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.shellming.sparkathon_mvp.models.LocationModel;
import com.shellming.sparkathon_mvp.models.Weather;
import com.shellming.sparkathon_mvp.models.WeatherModel;
import com.shellming.sparkathon_mvp.presenters.ICurrentObPresenter;
import com.shellming.sparkathon_mvp.utils.ToastUtil;
import com.shellming.sparkathon_mvp.views.ICurrentObView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ruluo1992 on 2/21/2016.
 */
public class CurrentObPresenter implements ICurrentObPresenter {
    private ICurrentObView currentObView;

    public CurrentObPresenter(ICurrentObView currentObView) {
        this.currentObView = currentObView;
    }

    @Override
    public void onCreateDone() {
        refreshData();
    }

    @Override
    public void refreshData() {
        currentObView.startRefresh();
        Location location = LocationModel.getInstance().getLocation(currentObView.getContext());
        WeatherModel.getInstance().getObWeatherCurrent(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Weather>() {
                    @Override
                    public void onCompleted() {
                        currentObView.stopRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(currentObView.getContext(), "获取天气失败！", Toast.LENGTH_SHORT);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Weather weather) {
                        currentObView.setData(weather);
                    }
                });
    }

    @Override
    public void clickYes() {

    }

    @Override
    public void clickNo() {

    }
}
