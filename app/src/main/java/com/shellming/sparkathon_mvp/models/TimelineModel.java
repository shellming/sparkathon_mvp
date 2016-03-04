package com.shellming.sparkathon_mvp.models;

import android.content.Context;
import android.location.Location;

import com.shellming.sparkathon_mvp.constants.GlobalConstant;
import com.shellming.sparkathon_mvp.utils.TwitterUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by ruluo1992 on 2/29/2016.
 */
public class TimelineModel {
    private static TimelineModel timelineModel = new TimelineModel();

    public static TimelineModel getInstance(){
        return timelineModel;
    }

    public Observable<List<Timeline>> getObTimeline(Context context){
        return Observable.just(context)
                .map(new Func1<Context, List<Timeline>>() {
                    @Override
                    public List<Timeline> call(Context context) {
                        try {
                            return getTimeline(context);
                        } catch (TwitterException e) {
                            throw Exceptions.propagate(e);
                        }
                    }
                });
    }

    public Observable<String> sendObTimeline(final Context context, Timeline timeline){
        return Observable.just(timeline)
                .map(new Func1<Timeline, String>() {
                    @Override
                    public String call(Timeline timeline) {
                        StatusUpdate update = new StatusUpdate(timeline.toString());
                        Location location = LocationModel.getInstance().getLocation(context);

                        Double latitude = location.getLatitude();
                        Double longitude = location.getLongitude();
                        GeoLocation geoLocation = new GeoLocation(latitude, longitude);
                        update.setLocation(geoLocation);
                        Twitter twitter = TwitterUtil.getInstance().getTwitter();
                        try {
                            twitter.updateStatus(update);
                            return "更新成功！";
                        } catch (TwitterException e) {
                            throw Exceptions.propagate(e);
                        }
                    }
                });
    }

    private List<Timeline> getTimeline(Context context) throws TwitterException {
        Query query = new Query(GlobalConstant.TWITTER_TAG);
        Twitter twitter = TwitterUtil.getInstance().getTwitter();
        QueryResult result = twitter.search(query);
        List<twitter4j.Status> tweets = result.getTweets();
        List<twitter4j.Status> statuses = new ArrayList<>();
        for(int i = 0; i < tweets.size(); i++){
            twitter4j.Status status = twitter.showStatus(tweets.get(i).getId());
            statuses.add(status);
        }
        List<Timeline> models = new ArrayList<>();
        for(int i = 0; i < statuses.size(); i++){
            Timeline model = Timeline.fromTwitter(statuses.get(i));
            if(model != null)
                models.add(model);
        }
        return models;
    }
}
