package com.shellming.sparkathon_mvp.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.shellming.sparkathon_mvp.utils.TwitterUtil;

import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by ruluo1992 on 2/20/2016.
 */
public class UserModel {
    private static UserModel userModel = new UserModel();

    private static final String LOGIN_STATUS = "LOGIN_STATUS";
    private static final String TWITTER_TOKEN = "TWITTER_TOKEN";
    private static final String TWITTER_TOKEN_SECRET = "TWITTER_TOKEN_SECRET";

    public static UserModel getInstance(){
        return userModel;
    }

    private User loginUser;

    public Observable<User> getObLogin(final Context context, final String oauthToken){
        return Observable.just(oauthToken)
                .map(new Func1<String, User>() {
                    @Override
                    public User call(String o) {
                        try{
                            return login(context, oauthToken);
                        }catch (Exception e){
                            throw Exceptions.propagate(e);
                        }
                    }
                });
    }

    public Observable<User> getObLoginUser(final Context context){
        return Observable.just("")
                .map(new Func1<String, User>() {
                    @Override
                    public User call(String s) {
                        try{
                            return getLoginUserInfo(context);
                        }catch (Exception e){
                            throw Exceptions.propagate(e);
                        }
                    }
                });
    }

    public Observable<RequestToken> getObRequestToken(final Context context){
        return Observable.just("")
                .map(new Func1<String, RequestToken>() {
                    @Override
                    public RequestToken call(String s) {
                        return TwitterUtil.getInstance().getRequestToken();
                    }
                });
    }

    public boolean isLogin(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(LOGIN_STATUS, false);
    }

    public void logout(Context context){
        clearLoginStatus(context);
        loginUser = null;
    }

    private User login(Context context, String oauthToken) throws TwitterException {
        // get login user
        Twitter twitter = TwitterUtil.getInstance().getTwitter();
        RequestToken requestToken = TwitterUtil.getInstance().getRequestToken();
        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, oauthToken);
        twitter.setOAuthAccessToken(accessToken);
        twitter4j.User twitterUser = twitter.showUser(accessToken.getUserId());
        loginUser = User.fromTwitterUser(twitterUser);

        // save user status
        saveLoginStatus(context, accessToken.getToken(), accessToken.getTokenSecret());
        return loginUser;
    }

    private void saveLoginStatus(Context context, String token, String tokenSecret){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_STATUS, true);
        editor.putString(TWITTER_TOKEN, token);
        editor.putString(TWITTER_TOKEN_SECRET, tokenSecret);
        editor.commit();
    }

    private void clearLoginStatus(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_STATUS, false);
        editor.putString(TWITTER_TOKEN, "");
        editor.putString(TWITTER_TOKEN_SECRET, "");
        editor.commit();
        TwitterUtil.getInstance().reset();
    }

    private User getLoginUserInfo(Context context) throws TwitterException {
        if(loginUser != null)
            return loginUser;
        if(isLogin(context)){
            Twitter twitter = TwitterUtil.getInstance().getTwitter();
            AccessToken accessToken = getAccessToken(context);
            twitter.setOAuthAccessToken(accessToken);
            return User.fromTwitterUser(twitter.showUser(accessToken.getUserId()));
        }
        return null;
    }

    private AccessToken getAccessToken(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = sharedPreferences.getString(TWITTER_TOKEN, "");
        String tokenSecret = sharedPreferences.getString(TWITTER_TOKEN_SECRET, "");
        if("".equals(token) || "".equals(tokenSecret))
            return null;
        AccessToken accessToken = new AccessToken(token, tokenSecret);
        return accessToken;
    }
}
