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

import com.shellming.sparkathon_mvp.R;
import com.shellming.sparkathon_mvp.models.Weather;
import com.shellming.sparkathon_mvp.presenters.ITendaysForcastPresenter;
import com.shellming.sparkathon_mvp.presenters.adapter.TendayViewAdaper;
import com.shellming.sparkathon_mvp.presenters.presenter.TendaysForcastPresenter;
import com.shellming.sparkathon_mvp.views.ITendaysForcastView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruluo1992 on 12/10/2015.
 */
public class TenDayForcastFragment extends Fragment implements ITendaysForcastView, SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TendayViewAdaper adapter;

    private ITendaysForcastPresenter tendaysForcastPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView =inflater.inflate(R.layout.fragment_tenday, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        setupRecylerView();
        setupSwipeRefreshLayout();

        tendaysForcastPresenter.onCreateDone();
        return mView;
    }

    private void setupRecylerView(){
        try {
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new TendayViewAdaper(new ArrayList<Weather>(), getContext());
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tendaysForcastPresenter = new TendaysForcastPresenter(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setData(List<Weather> weathers) {
        adapter.setData(weathers);
    }

    @Override
    public void startRefresh() {
        if(!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void stopRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        tendaysForcastPresenter.refreshData();
    }
}
