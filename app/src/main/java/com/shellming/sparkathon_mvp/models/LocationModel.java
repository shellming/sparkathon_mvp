package com.shellming.sparkathon_mvp.models;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.gson.Gson;
import com.shellming.sparkathon_mvp.constants.GlobalConstant;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by ruluo1992 on 2/18/2016.
 */
public class LocationModel {

    private static LocationModel locationModel = new LocationModel();
    private static OkHttpClient client = new OkHttpClient();
    private static final String CITY_URL_TEMPLATE = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&sensor=true";

    private String locationName;
    private Location location;
    private ReadWriteLock locationLock;

    public static LocationModel getInstance() {
        return locationModel;
    }

    public LocationModel() {
        locationLock = new ReentrantReadWriteLock();
    }

    public Observable<String> getObLocationName(final Context context){
        return Observable
                .just("")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        try {
                            return getLocationName(context);
                        } catch (Exception e) {
                            throw Exceptions.propagate(e);
                        }
                    }
                });
    }

    private String getLocationName(Context context) throws IOException {
        if(locationName != null &&
                !GlobalConstant.DEFAULT_CITYNAME.equals(locationName))
            return locationName;

        if(location == null)
            getLocation(context);
        if(location == null)
            return GlobalConstant.DEFAULT_CITYNAME;

        String latitude = String.format("%.2f", location.getLatitude());
        String longitude = String.format("%.2f", location.getLongitude());

        String url = String.format(CITY_URL_TEMPLATE, latitude, longitude);
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.setReadTimeout(15, TimeUnit.SECONDS);

        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        Response response = call.execute();
        locationName = getNameFromResponse(response.body().string());

        return locationName;
    }

    public Location getLocation(Context context){
        locationLock.readLock().lock();
        if(location != null)
            return location;
        locationLock.readLock().unlock();
        // 获取位置管理服务
        locationLock.writeLock().lock();
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) context.getSystemService(serviceName);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        location = getLastKnownLocation(locationManager);
        locationLock.writeLock().unlock();
        return location;
    }

    private Location getLastKnownLocation(LocationManager mLocationManager) {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private String getNameFromResponse(String response){
        Gson gson = new Gson();
        Map map = gson.fromJson(response, HashMap.class);

        List<Map> results = (List<Map>) map.get("results");
        Map result = results.get(0);
        String addr = result.get("formatted_address").toString();
        String[] parts = addr.split(",");

        String city = "";
        if(parts.length >= 3)
            city = parts[parts.length - 3];
        else
            city = parts[parts.length - 1];
        return city;
    }

}
