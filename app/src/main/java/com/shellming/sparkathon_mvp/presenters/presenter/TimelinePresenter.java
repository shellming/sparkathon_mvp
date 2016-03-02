package com.shellming.sparkathon_mvp.presenters.presenter;

import android.content.Intent;
import android.widget.Toast;

import com.shellming.sparkathon_mvp.models.Timeline;
import com.shellming.sparkathon_mvp.models.TimelineModel;
import com.shellming.sparkathon_mvp.models.UserModel;
import com.shellming.sparkathon_mvp.presenters.ITimelinePresenter;
import com.shellming.sparkathon_mvp.utils.ToastUtil;
import com.shellming.sparkathon_mvp.views.ITimelineView;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ruluo1992 on 2/29/2016.
 */
public class TimelinePresenter implements ITimelinePresenter {
    private ITimelineView timelineView;

    public TimelinePresenter(ITimelineView timelineView) {
        this.timelineView = timelineView;
    }

    @Override
    public void onCreateDone() {
        refreshData();
    }

    @Override
    public void refreshData() {
        if(UserModel.getInstance().isLogin(timelineView.getContext())){
            timelineView.startRefresh();
            TimelineModel.getInstance().getObTimeline(timelineView.getContext())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Timeline>>() {
                        @Override
                        public void onCompleted() {
                            timelineView.stopRefresh();
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.showToast(timelineView.getContext(), "获取失败！", Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onNext(List<Timeline> timelines) {
                            timelineView.setData(timelines);
                        }
                    });
        }
    }
}
