package com.shellming.sparkathon_mvp.models;

/**
 * Created by ruluo1992 on 2/20/2016.
 */
public class User {
    private String userName;
    private String avatar;
    private String userSig;

    public User(String avatar, String userName, String userSig) {
        this.avatar = avatar;
        this.userName = userName;
        this.userSig = userSig;
    }

    public static User fromTwitterUser(twitter4j.User twitterUser){
        String avatar = twitterUser.getProfileImageURL();
        String username = twitterUser.getName();
        String sign = twitterUser.getDescription();
        return new User(avatar, username, sign);
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }
}
