package com.shellming.sparkathon_mvp.presenters.presenter;

import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.shellming.sparkathon_mvp.models.LocationModel;
import com.shellming.sparkathon_mvp.models.Weather;
import com.shellming.sparkathon_mvp.models.WeatherModel;
import com.shellming.sparkathon_mvp.presenters.ITendaysForcastPresenter;
import com.shellming.sparkathon_mvp.utils.ToastUtil;
import com.shellming.sparkathon_mvp.views.ITendaysForcastView;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ruluo1992 on 2/29/2016.
 */
public class TendaysForcastPresenter implements ITendaysForcastPresenter {
    private ITendaysForcastView tendaysForcastView;

    public TendaysForcastPresenter(ITendaysForcastView tendaysForcastView) {
        this.tendaysForcastView = tendaysForcastView;
    }

    @Override
    public void onCreateDone() {
        refreshData();
    }

    @Override
    public void refreshData() {
        Location location = LocationModel.getInstance().getLocation(tendaysForcastView.getContext());
        tendaysForcastView.startRefresh();
        WeatherModel.getInstance().getWeatherTendaysForcast(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Weather>>() {
                    @Override
                    public void onCompleted() {
                        tendaysForcastView.stopRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(tendaysForcastView.getContext(), "获取天气失败！", Toast.LENGTH_SHORT);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Weather> weathers) {
                        tendaysForcastView.setData(weathers);
                    }
                });
    }
}
