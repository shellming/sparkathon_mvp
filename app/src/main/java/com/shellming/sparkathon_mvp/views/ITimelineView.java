package com.shellming.sparkathon_mvp.views;

import com.shellming.sparkathon_mvp.models.Timeline;

import java.util.List;

/**
 * Created by ruluo1992 on 2/29/2016.
 */
public interface ITimelineView extends IView {
    public void startRefresh();
    public void stopRefresh();
    public void setData(List<Timeline> timelines);
}
