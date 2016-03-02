package com.shellming.sparkathon_mvp.views.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shellming.sparkathon_mvp.R;
import com.shellming.sparkathon_mvp.constants.GlobalConstant;
import com.shellming.sparkathon_mvp.mappers.ViewPresenterMapper;
import com.shellming.sparkathon_mvp.models.User;
import com.shellming.sparkathon_mvp.presenters.IMainPresenter;
import com.shellming.sparkathon_mvp.views.IMainView;
import com.shellming.sparkathon_mvp.views.fragment.IndexFragment;
import com.shellming.sparkathon_mvp.views.fragment.TimelineFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements IMainView, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private CircleImageView mAvatar;
    private TextView mUsername;
    private TextView mUserSig;
    private TextView mExtraInfo;
    private IMainPresenter mainPresenter;
    private int currentSelected;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        Uri uri = intent.getData();
        if(uri != null && uri.toString().startsWith(GlobalConstant.TWITTER_CALLBACK_URL)) {
            String oauthVerifier = uri.getQueryParameter("oauth_verifier");
            mainPresenter.userLogin(oauthVerifier);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mainPresenter = ViewPresenterMapper.getMainPresenter(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nagivation);
        View headerView = navigationView.getHeaderView(0);
        mAvatar = (CircleImageView) headerView.findViewById(R.id.profile_image);
        mUsername = (TextView) headerView.findViewById(R.id.username);
        mUserSig = (TextView) headerView.findViewById(R.id.usersig);
        mExtraInfo = (TextView) headerView.findViewById(R.id.extra);

        setupToolbar();
        setupDrawerLayout();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new IndexFragment());
        transaction.commit();

        mainPresenter.onCreateDone();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewPresenterMapper.setMainPresenter(null);
    }

    @Override
    public void setData(User user) {
        mUsername.setText("@" + user.getUserName());
        mUserSig.setText(user.getUserSig());
        ImageLoader.getInstance().displayImage(user.getAvatar(), mAvatar);
        mExtraInfo.setText(GlobalConstant.DEFAULT_USER_EXTRA_INFO);
    }

    @Override
    public void clearData() {
        mUsername.setText(GlobalConstant.DEFAULT_USER_NAME);
        mUserSig.setText("");
        mExtraInfo.setText("");
        mAvatar.setImageResource(GlobalConstant.DEFAULT_AVATAR);
    }

    private void setupDrawerLayout(){
        currentSelected = R.id.index;
        mAvatar.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setCityName(String name) {
        getSupportActionBar().setTitle(name);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_image:
                mainPresenter.clickAvatar();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        if(menuItem.getItemId() != currentSelected) {
            currentSelected = menuItem.getItemId();
            Fragment fragment = null;
            switch (currentSelected){
                case R.id.index:
                    fragment = new IndexFragment();
                    break;
                case R.id.timeline:
                    fragment = new TimelineFragment();
                    break;
            }
            if(fragment != null){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        }
        mDrawerLayout.closeDrawers();
        return true;
    }
}
