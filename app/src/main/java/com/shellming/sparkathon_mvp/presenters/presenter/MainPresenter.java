package com.shellming.sparkathon_mvp.presenters.presenter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.shellming.sparkathon_mvp.R;
import com.shellming.sparkathon_mvp.constants.GlobalConstant;
import com.shellming.sparkathon_mvp.models.LocationModel;
import com.shellming.sparkathon_mvp.models.User;
import com.shellming.sparkathon_mvp.models.UserModel;
import com.shellming.sparkathon_mvp.presenters.IMainPresenter;
import com.shellming.sparkathon_mvp.utils.ToastUtil;
import com.shellming.sparkathon_mvp.views.IMainView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import twitter4j.auth.RequestToken;

/**
 * Created by ruluo1992 on 2/18/2016.
 */
public class MainPresenter implements IMainPresenter {
    private IMainView mainView;

    public MainPresenter(IMainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCreateDone() {
        // 获取并显示当前城市名
        LocationModel.getInstance()
                .getObLocationName(mainView.getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mainView.setCityName(GlobalConstant.DEFAULT_CITYNAME);
                        ToastUtil.showToast(mainView.getContext(), "获取城市名失败！", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onNext(String s) {
                        mainView.setCityName(s);
                    }
                });
        if(UserModel.getInstance().isLogin(mainView.getContext())){
            UserModel.getInstance().getObLoginUser(mainView.getContext())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<User>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.showToast(mainView.getContext(), "获取用户信息失败！", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(User user) {
                            mainView.setData(user);
                        }
                    });
        }
        init();
    }

    private void init(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mainView.getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void clickAvatar() {
        if(UserModel.getInstance().isLogin(mainView.getContext())){
            UserModel.getInstance().logout(mainView.getContext());
            mainView.clearData();
        }
        else{
            UserModel.getInstance().getObRequestToken(mainView.getContext())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<RequestToken>() {
                        @Override
                        public void call(RequestToken requestToken) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
                            mainView.getContext().startActivity(intent);
                        }
                    });
        }
    }

    @Override
    public void userLogin(String oauthVerifier) {
        UserModel.getInstance().getObLogin(mainView.getContext(), oauthVerifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        mainView.setData(user);
                    }
                });
    }

}
