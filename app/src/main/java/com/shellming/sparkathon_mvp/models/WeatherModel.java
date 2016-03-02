package com.shellming.sparkathon_mvp.models;

import android.location.Location;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by ruluo1992 on 2/21/2016.
 */
public class WeatherModel {
    private static WeatherModel weatherModel = new WeatherModel();

    public static WeatherModel getInstance(){
        return weatherModel;
    }

    private String URL_TEMPLATE = "http://45.78.49.159/api/weather%s?units=m&geocode=%s,%s&language=en-US";
    private String CURRENT = "/v2/observations/current";
    private String TEN_DAYS_LATER = "/v2/forecast/daily/10day";
    private String ONE_DAY_LATER = "/v2/forecast/hourly/24hour";
    private String WEATHER_USER = "449ed3cf-b721-4d15-b8ff-ee932aecc63a";
    private String WEATHER_PASS = "Rb9LMNclIq";

    private OkHttpClient client = new OkHttpClient();

    public Observable<Weather> getObWeatherCurrent(Location location){
        return Observable.just(location)
                .map(new Func1<Location, Weather>() {
                    @Override
                    public Weather call(Location location) {
                        String latitude = String.format("%.2f", location.getLatitude());
                        String longitude = String.format("%.2f", location.getLongitude());
                        try {
                            Weather weather = getWeatherCurrent(latitude, longitude);
                            return weather;
                        } catch (IOException e) {
                            throw Exceptions.propagate(e);
                        }
                    }
                });
    }

    public Observable<List<Weather>> getObWeatherOnedayForcast(Location location){
        return Observable.just(location)
                .map(new Func1<Location, List<Weather>>() {
                    @Override
                    public List<Weather> call(Location location) {
                        String latitude = String.format("%.2f", location.getLatitude());
                        String longitude = String.format("%.2f", location.getLongitude());
                        try {
                            return getWeatherOnedayForcast(latitude, longitude);
                        } catch (IOException e) {
                            throw Exceptions.propagate(e);
                        }
                    }
                });
    }

    public Observable<List<Weather>> getWeatherTendaysForcast(Location location){
        return Observable.just(location)
                .map(new Func1<Location, List<Weather>>() {
                    @Override
                    public List<Weather> call(Location location) {
                        String latitude = String.format("%.2f", location.getLatitude());
                        String longitude = String.format("%.2f", location.getLongitude());
                        try {
                            return getWeatherTendaysForcast(latitude, longitude);
                        } catch (IOException e) {
                            throw Exceptions.propagate(e);
                        }
                    }
                });
    }

    private List<Weather> getWeatherTendaysForcast(String latitude, String longitude) throws IOException {
        String url = String.format(URL_TEMPLATE, TEN_DAYS_LATER, latitude, longitude);
        String response = getWeatherResponse(url);
        List<Weather> weathers = Weather.fromTendaysForcastResponse(response);
        return weathers;
    }

    private Weather getWeatherCurrent(String latitude, String longitude) throws IOException {
        String url = String.format(URL_TEMPLATE, CURRENT, latitude, longitude);
        String response = getWeatherResponse(url);
        Weather weather = Weather.fromCurrentObResponse(response);
        return weather;
    }

    private List<Weather> getWeatherOnedayForcast(String latitude, String longitude) throws IOException {
        String url = String.format(URL_TEMPLATE, ONE_DAY_LATER, latitude, longitude);
        String response = getWeatherResponse(url);
        return Weather.fromOnedayForcastResponse(response);
    }

    private String getWeatherResponse(String url) throws IOException {
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.setReadTimeout(15, TimeUnit.SECONDS);
        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                String credential = Credentials.basic(WEATHER_USER, WEATHER_PASS);
                return response.request().newBuilder().header("Authorization", credential).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return null;
            }
        });

        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        Response response = call.execute();
        return response.body().string();
    }
}
