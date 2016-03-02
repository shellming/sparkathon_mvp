package com.shellming.sparkathon_mvp.presenters.presenter;

import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.shellming.sparkathon_mvp.models.LocationModel;
import com.shellming.sparkathon_mvp.models.Weather;
import com.shellming.sparkathon_mvp.models.WeatherModel;
import com.shellming.sparkathon_mvp.presenters.IOnedayForcastPresenter;
import com.shellming.sparkathon_mvp.utils.ToastUtil;
import com.shellming.sparkathon_mvp.views.IOneDayForcastView;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ruluo1992 on 2/28/2016.
 */
public class OnedayForcastPresenter implements IOnedayForcastPresenter {
    private IOneDayForcastView mView;

    public OnedayForcastPresenter(IOneDayForcastView mView) {
        this.mView = mView;
    }

    @Override
    public void onCreateDone() {
        refreshData();
    }


    @Override
    public void refreshData() {
        mView.startRefresh();
        Location location = LocationModel.getInstance().getLocation(mView.getContext());
        WeatherModel.getInstance().getObWeatherOnedayForcast(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Weather>>() {
                    @Override
                    public void onCompleted() {
                        mView.stopRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(mView.getContext(), "获取天气数据失败！", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onNext(List<Weather> weathers) {
                        mView.setData(weathers);
                    }
                });
    }
}
