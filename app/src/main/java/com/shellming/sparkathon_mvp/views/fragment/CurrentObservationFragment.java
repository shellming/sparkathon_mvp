package com.shellming.sparkathon_mvp.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.shellming.sparkathon_mvp.R;
import com.shellming.sparkathon_mvp.models.Weather;
import com.shellming.sparkathon_mvp.presenters.ICurrentObPresenter;
import com.shellming.sparkathon_mvp.presenters.adapter.CurrentObAdapter;
import com.shellming.sparkathon_mvp.presenters.presenter.CurrentObPresenter;
import com.shellming.sparkathon_mvp.views.ICurrentObView;

/**
 * Created by ruluo1992 on 12/12/2015.
 */
public class CurrentObservationFragment extends Fragment implements ICurrentObView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CurrentObAdapter adapter;

    private TextView mTempView;
    private TextView mDesView;
    private ImageView mWeatherImgView;
    private TextView mDateTime;
    private TextView mDegreeView;

    private FloatingActionsMenu actionsMenu;

    private ICurrentObPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CurrentObPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_current, container, false);
        recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.recyclerview);
        mTempView = (TextView) swipeRefreshLayout.findViewById(R.id.tempture);
        mDesView = (TextView) swipeRefreshLayout.findViewById(R.id.description);
        mWeatherImgView = (ImageView) swipeRefreshLayout.findViewById(R.id.weatherImg);
        mDateTime = (TextView) swipeRefreshLayout.findViewById(R.id.data_time);
        mDegreeView = (TextView) swipeRefreshLayout.findViewById(R.id.degree);

        actionsMenu = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions);

        setupRecylerView();
        setupSwipeRefreshLayout();

        presenter.onCreateDone();
        return swipeRefreshLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startRefresh() {
        if(swipeRefreshLayout.isRefreshing())
            return;
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void stopRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public FloatingActionsMenu getFloatingActionMenu() {
        return actionsMenu;
    }

    @Override
    public void setData(Weather weather) {
        adapter.setData(weather);
        System.out.println(weather.getTemp());
        mTempView.setText(weather.getTemp().toString());
        mDesView.setText(weather.getPhrase_32char());
        mWeatherImgView.setImageResource(weather.getIcon());
        mDateTime.setText(weather.getFormatedDate());
        mDegreeView.setText("℃");
    }

    private void setupRecylerView(){
        try {
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new CurrentObAdapter();
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSwipeRefreshLayout(){
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        presenter.refreshData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_right:
                presenter.clickYes();
                break;
            case R.id.action_wrong:
                presenter.clickNo();
                break;
        }
    }
}
