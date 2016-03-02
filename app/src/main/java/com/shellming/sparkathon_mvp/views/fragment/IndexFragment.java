package com.shellming.sparkathon_mvp.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.shellming.sparkathon_mvp.R;
import com.shellming.sparkathon_mvp.presenters.adapter.MainViewPagerAdapter;
import com.shellming.sparkathon_mvp.views.IIndexView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruluo1992 on 11/27/2015.
 */
public class IndexFragment extends Fragment implements IIndexView, ViewPager.OnPageChangeListener {
    private MainViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private FloatingActionsMenu actionsMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_index, container, false);

        viewPager = (ViewPager) mView.findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) mView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        actionsMenu = (FloatingActionsMenu) getActivity().findViewById(R.id.multiple_actions);
        actionsMenu.setVisibility(View.VISIBLE);

        return mView;
    }

    private void setupViewPager(ViewPager viewPager) {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        fragments.add(new CurrentObservationFragment());
        fragments.add(new OneDayForcastFragment());
        fragments.add(new TenDayForcastFragment());

        titles.add("current");
        titles.add("24hours");
        titles.add("10days");

        viewPagerAdapter = new MainViewPagerAdapter(getFragmentManager(), fragments, titles);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position == 0){
            actionsMenu.setTranslationY(
                    positionOffset * actionsMenu.getHeight()
            );
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
