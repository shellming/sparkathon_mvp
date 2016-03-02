package com.shellming.sparkathon_mvp.mappers;

import com.shellming.sparkathon_mvp.presenters.ICurrentObPresenter;
import com.shellming.sparkathon_mvp.presenters.IMainPresenter;
import com.shellming.sparkathon_mvp.presenters.IOnedayForcastPresenter;
import com.shellming.sparkathon_mvp.presenters.ITendaysForcastPresenter;
import com.shellming.sparkathon_mvp.presenters.ITimelinePresenter;
import com.shellming.sparkathon_mvp.presenters.presenter.CurrentObPresenter;
import com.shellming.sparkathon_mvp.presenters.presenter.MainPresenter;
import com.shellming.sparkathon_mvp.presenters.presenter.OnedayForcastPresenter;
import com.shellming.sparkathon_mvp.presenters.presenter.TendaysForcastPresenter;
import com.shellming.sparkathon_mvp.presenters.presenter.TimelinePresenter;
import com.shellming.sparkathon_mvp.views.ICurrentObView;
import com.shellming.sparkathon_mvp.views.IMainView;
import com.shellming.sparkathon_mvp.views.IOneDayForcastView;
import com.shellming.sparkathon_mvp.views.ITendaysForcastView;
import com.shellming.sparkathon_mvp.views.ITimelineView;

/**
 * Created by ruluo1992 on 2/19/2016.
 */
public class ViewPresenterMapper {
    private static IMainPresenter mainPresenter;
    private static ICurrentObPresenter currentObPresenter;
    private static IOnedayForcastPresenter onedayForcastPresenter;
    private static ITendaysForcastPresenter tendaysForcastPresenter;
    private static ITimelinePresenter timelinePresenter;

    public static ITimelinePresenter getTimelinePresenter(ITimelineView timelineView){
        if(timelinePresenter == null)
            timelinePresenter = new TimelinePresenter(timelineView);
        return timelinePresenter;
    }

    public static void setTimelinePresenter(ITimelinePresenter timelinePresenter) {
        ViewPresenterMapper.timelinePresenter = timelinePresenter;
    }

    public static ICurrentObPresenter getCurrentObPresenter(ICurrentObView currentObView) {
        if(currentObPresenter == null)
            currentObPresenter = new CurrentObPresenter(currentObView);
        return currentObPresenter;
    }

    public static IMainPresenter getMainPresenter(IMainView mainView) {
        if(mainPresenter == null)
            mainPresenter = new MainPresenter(mainView);
        return mainPresenter;
    }

    public static IOnedayForcastPresenter getOnedayForcastPresenter(IOneDayForcastView oneDayForcastView) {
        if(onedayForcastPresenter == null)
            onedayForcastPresenter = new OnedayForcastPresenter(oneDayForcastView);
        return onedayForcastPresenter;
    }

    public static ITendaysForcastPresenter getTendaysForcastPresenter(ITendaysForcastView tendaysForcastView) {
        if(tendaysForcastPresenter == null)
            tendaysForcastPresenter = new TendaysForcastPresenter(tendaysForcastView);
        return tendaysForcastPresenter;
    }

    public static void setCurrentObPresenter(ICurrentObPresenter currentObPresenter) {
        ViewPresenterMapper.currentObPresenter = currentObPresenter;
    }

    public static void setMainPresenter(IMainPresenter mainPresenter) {
        ViewPresenterMapper.mainPresenter = mainPresenter;
    }

    public static void setOnedayForcastPresenter(IOnedayForcastPresenter onedayForcastPresenter) {
        ViewPresenterMapper.onedayForcastPresenter = onedayForcastPresenter;
    }

    public static void setTendaysForcastPresenter(ITendaysForcastPresenter tendaysForcastPresenter) {
        ViewPresenterMapper.tendaysForcastPresenter = tendaysForcastPresenter;
    }
}
