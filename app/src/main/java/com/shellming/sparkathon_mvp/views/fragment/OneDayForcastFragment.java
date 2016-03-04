package com.shellming.sparkathon_mvp.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.shellming.sparkathon_mvp.R;
import com.shellming.sparkathon_mvp.models.Weather;
import com.shellming.sparkathon_mvp.presenters.IOnedayForcastPresenter;
import com.shellming.sparkathon_mvp.presenters.presenter.OnedayForcastPresenter;
import com.shellming.sparkathon_mvp.views.IOneDayForcastView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruluo1992 on 12/10/2015.
 */
public class OneDayForcastFragment extends Fragment implements IOneDayForcastView, SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private LineChart temperatureChart;
    private LineChart windChart;
    private LineChart wetChart;
    private LineChart visitChart;
    private LineChart pressureChart;
    private View mView;

    private IOnedayForcastPresenter onedayForcastPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onedayForcastPresenter = new OnedayForcastPresenter(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setData(String title, List<Entry> entries, List<String> labels, LineChart chart, int index){
        LineDataSet dataSet = new LineDataSet(entries, title);
        dataSet.setLineWidth(5.0f);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[index]);
        LineData data = new LineData(labels, dataSet);
        data.addDataSet(dataSet);
        chart.setData(data);
        chart.invalidate();
    }

    private void setupChart(LineChart chart){
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setStartAtZero(false);

        yAxis = chart.getAxisRight();
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);
        yAxis.setDrawAxisLine(false);

        chart.setDescription("");
        chart.setDrawGridBackground(false);
        chart.getLegend().setEnabled(false);
        chart.setClickable(false);
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerTapEnabled(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_oneday, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_layout);
        temperatureChart = (LineChart) mView.findViewById(R.id.temperature_chart);
        windChart = (LineChart) mView.findViewById(R.id.wind_chart);
        wetChart = (LineChart) mView.findViewById(R.id.wet_chart);
        visitChart = (LineChart) mView.findViewById(R.id.visit_chart);
        pressureChart = (LineChart) mView.findViewById(R.id.pressure_chart);

        setupChart(temperatureChart);
        setupChart(windChart);
        setupChart(wetChart);
        setupChart(visitChart);
        setupChart(pressureChart);
        setupSwipeRefreshLayout();

        onedayForcastPresenter.onCreateDone();

        return mView;
    }

    @Override
    public void setData(List<Weather> weathers) {
        List<String> labels = new ArrayList<>();
        List<Entry> temperatureEnties = new ArrayList<>();
        List<Entry> windEnties = new ArrayList<>();
        List<Entry> wetEnties = new ArrayList<>();
        List<Entry> visitEnties = new ArrayList<>();
        List<Entry> pressureEnties = new ArrayList<>();

        for(int i = 0; i < weathers.size(); i++){
            Weather weather = weathers.get(i);
            int x = Integer.valueOf(weather.getPredictHour());
            float temperature = Float.valueOf(weather.getTemp());
            float windSpeed = Float.valueOf(weather.getWspd());
            float wet = Float.valueOf(weather.getRh());
            float visit = weather.getVis().floatValue();
            float pressure = weather.getMslp().floatValue();

            Entry entry = new Entry(temperature, i);
            temperatureEnties.add(entry);

            entry = new Entry(windSpeed, i);
            windEnties.add(entry);

            entry = new Entry(wet, i);
            wetEnties.add(entry);

            entry = new Entry(visit, i);
            visitEnties.add(entry);

            entry = new Entry(pressure, i);
            pressureEnties.add(entry);

            labels.add(String.valueOf(x));
        }

        setData("Temperature", temperatureEnties, labels, temperatureChart, 0);
        setData("WindSpeed", windEnties, labels, windChart, 1);
        setData("Humidity", wetEnties, labels, wetChart, 2);
        setData("Visibility", visitEnties, labels, visitChart, 3);
        setData("Pressure", pressureEnties, labels, pressureChart, 4);
    }

    private void setupSwipeRefreshLayout(){
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void startRefresh() {
        if(swipeRefreshLayout.isRefreshing())
            return;
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
        onedayForcastPresenter.refreshData();
    }
}
