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

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.shellming.sparkathon_mvp.R;
import com.shellming.sparkathon_mvp.models.Timeline;
import com.shellming.sparkathon_mvp.presenters.ITimelinePresenter;
import com.shellming.sparkathon_mvp.presenters.adapter.TimelineRecyclerViewAdapter;
import com.shellming.sparkathon_mvp.presenters.presenter.TimelinePresenter;
import com.shellming.sparkathon_mvp.views.ITimelineView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruluo1992 on 12/24/2015.
 */
public class TimelineFragment extends Fragment implements ITimelineView, SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private TimelineRecyclerViewAdapter adapter;

    private ITimelinePresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new TimelinePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_timeline, container, false);

        recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.timeline_list);
        adapter = new TimelineRecyclerViewAdapter(new ArrayList());
        setupSwipeRefreshLayout();
        setupRecylerView();

        FloatingActionsMenu actionsMenu = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions);
        actionsMenu.setVisibility(View.INVISIBLE);

        presenter.onCreateDone();
        return swipeRefreshLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setupSwipeRefreshLayout(){
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setupRecylerView(){
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
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
    public void setData(List<Timeline> timelines) {
        adapter.setData(timelines);
    }

    @Override
    public void onRefresh() {
        presenter.refreshData();
    }
}
