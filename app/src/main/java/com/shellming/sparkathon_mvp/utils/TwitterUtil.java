package com.shellming.sparkathon_mvp.utils;

import com.shellming.sparkathon_mvp.constants.GlobalConstant;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by ruluo1992 on 11/27/2015.
 */
public class TwitterUtil {
    private RequestToken requestToken = null;
    private TwitterFactory twitterFactory = null;
    private Twitter twitter;
    private TwitterStreamFactory twitterStreamFactory;
    private TwitterStream twitterStream;


    public TwitterUtil() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(GlobalConstant.TWITTER_CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(GlobalConstant.TWITTER_CONSUMER_SECRET);
        Configuration configuration = configurationBuilder.build();
        twitterFactory = new TwitterFactory(configuration);
        twitter = twitterFactory.getInstance();
        twitterStreamFactory = new TwitterStreamFactory(configuration);
        twitterStream = twitterStreamFactory.getInstance();
    }

    public void postTweet(String content){
//        new TwitterPostTweetTask().execute(content);
    }

    public void searchTweets(){
//        new TwitterSearchTweetsTask().execute(GlobalConstant.TWITTER_TAG);
    }

    public TwitterStream getTwitterStream() {
        return twitterStream;
    }

    public void setTwitterStream(TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    public TwitterFactory getTwitterFactory()
    {
        return twitterFactory;
    }

    public void setTwitterFactory(AccessToken accessToken)
    {
        twitter = twitterFactory.getInstance(accessToken);
    }

    public Twitter getTwitter()
    {
        return twitter;
    }

    public RequestToken getRequestToken() {
        if (requestToken == null) {
            try {
                requestToken = twitterFactory.getInstance().getOAuthRequestToken(GlobalConstant.TWITTER_CALLBACK_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return requestToken;
    }

    public static TwitterUtil instance = new TwitterUtil();

    public static TwitterUtil getInstance() {
        return instance;
    }

    public void reset() {
        instance = new TwitterUtil();
    }
}

