package com.shellming.sparkathon_mvp.models;

import com.google.gson.Gson;
import com.shellming.sparkathon_mvp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ruluo1992 on 2/21/2016.
 */
public class Weather {
    private Long obs_time;           //观测时间
    private String phrase_32char;     //天气描述，可以指定语言
    private String sunrise;         //日出时间，Example: 20130523T06:32:00-04:00
    private String sunset;          //日落时间，Example: 20130523T20:37:00-04:00
    private Integer icon_code;      //图标标号
    private Integer wspd;           //风速
    private Double vis;             //能见度
    private Double mslp;            //压强，毫巴
    private Integer temp;           //温度，摄氏度
    private Integer tempMax;
    private Integer tempMin;
    private Integer rh;             //相对湿度，%

    public static List<Weather> fromTendaysForcastResponse(String response){
        Gson gson = new Gson();
        Map resp = gson.fromJson(response, HashMap.class);
        List<Map> forecasts = (List<Map>) resp.get("forecasts");
        List<Weather> weathers = new ArrayList<>();

        for(int i = 1; i < forecasts.size(); i++){
            Map result = forecasts.get(i);
            Weather weather = new Weather();
            Double obsTime = (Double) result.get("fcst_valid");
            weather.setObs_time(obsTime.longValue());
            weather.setSunrise((String) result.get("sunrise"));
            weather.setSunset((String) result.get("sunset"));
            Double maxTemp = (Double) result.get("max_temp");
            weather.setTempMax(maxTemp.intValue());
            Double minTemp = (Double) result.get("min_temp");
            weather.setTempMin(minTemp.intValue());

            Map day = (Map) result.get("day");
            weather.setPhrase_32char((String) day.get("phrase_32char"));
            Double iconCode = (Double) day.get("icon_code");
            weather.setIcon_code(iconCode.intValue());
//            Double windSpeed = (Double) day.get("wspd");
//            weather.setWspd(windSpeed.intValue());
//            weather.setVis((Double) day.get("vis"));
//            weather.setMslp((Double) day.get("mslp"));
//            Double temp = (Double) result.get("temp");
//            weather.setTemp(temp.intValue());
//            Double wet = (Double) day.get("rh");
//            weather.setRh(wet.intValue());

            weathers.add(weather);
        }

        return weathers;
    }

    public static List<Weather> fromOnedayForcastResponse(String response){
        Gson gson = new Gson();
        Map resp = gson.fromJson(response, HashMap.class);
        List<Map> forecasts = (List<Map>) resp.get("forecasts");
        List<Weather> weathers = new ArrayList<>();

        for(int i = 0; i < forecasts.size(); i++){
            Map result = forecasts.get(i);
            Weather weather = new Weather();
            Double obsTime = (Double) result.get("fcst_valid");
            weather.setObs_time(obsTime.longValue());
            weather.setPhrase_32char((String) result.get("phrase_32char"));
            Double windSpeed = (Double) result.get("wspd");
            weather.setWspd(windSpeed.intValue());
            weather.setVis((Double) result.get("vis"));
            weather.setMslp((Double) result.get("mslp"));
            Double temp = (Double) result.get("temp");
            weather.setTemp(temp.intValue());
            Double wet = (Double) result.get("rh");
            weather.setRh(wet.intValue());

            weathers.add(weather);
        }

        return weathers;
    }

    public static Weather fromCurrentObResponse(String response){
        Gson gson = new Gson();
        Map resp = gson.fromJson(response, HashMap.class);
        Map result = (Map) resp.get("observation");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!response:" + response);

        Weather weather = new Weather();
        Double obsTime = (Double) result.get("obs_time");
        weather.setObs_time(obsTime.longValue());
        weather.setPhrase_32char((String) result.get("phrase_32char"));
        weather.setSunrise((String) result.get("sunrise"));
        weather.setSunset((String) result.get("sunset"));
        Double iconCode = (Double) result.get("icon_code");
        weather.setIcon_code(iconCode.intValue());

        Map metric = (Map) result.get("metric");
        Double windSpeed = (Double) metric.get("wspd");
        weather.setWspd(windSpeed.intValue());
        weather.setVis((Double) metric.get("vis"));
        weather.setMslp((Double) metric.get("mslp"));
        Double temp = (Double) metric.get("temp");
        weather.setTemp(temp.intValue());
        Double wet = (Double) metric.get("rh");
        weather.setRh(wet.intValue());

        return weather;
    }

    public Integer getTempMax() {
        return tempMax;
    }

    public void setTempMax(Integer tempMax) {
        this.tempMax = tempMax;
    }

    public Integer getTempMin() {
        return tempMin;
    }

    public void setTempMin(Integer tempMin) {
        this.tempMin = tempMin;
    }

    public Integer getIcon_code() {
        return icon_code;
    }

    public void setIcon_code(Integer icon_code) {
        this.icon_code = icon_code;
    }

    public Double getMslp() {
        return mslp;
    }

    public void setMslp(Double mslp) {
        this.mslp = mslp;
    }

    public Long getObs_time() {
        return obs_time;
    }

    public void setObs_time(Long obs_time) {
        this.obs_time = obs_time;
    }

    public String getPhrase_32char() {
        return phrase_32char;
    }

    public void setPhrase_32char(String phrase_32char) {
        this.phrase_32char = phrase_32char;
    }

    public Integer getRh() {
        return rh;
    }

    public void setRh(Integer rh) {
        this.rh = rh;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Double getVis() {
        return vis;
    }

    public void setVis(Double vis) {
        this.vis = vis;
    }

    public Integer getWspd() {
        return wspd;
    }

    public void setWspd(Integer wspd) {
        this.wspd = wspd;
    }

    public Integer getIcon() {
        return getWeatherImgRes(icon_code);
    }

    public int getPredictHour(){
        Date d = new Date(obs_time * 1000);
        //  首先按照d(日)格式化获取日期。
        SimpleDateFormat format = new SimpleDateFormat("HH");
        String temp = format.format(d);
        return Integer.valueOf(temp);
    }

    public String getFormatedDate(){
        Date d = new Date(obs_time * 1000);
        //  首先按照d(日)格式化获取日期。
        SimpleDateFormat format = new SimpleDateFormat("d");
        String temp = format.format(d);
        //判断日期如果为1结尾且不是11则 用"dd'st'MMMMyyyy,EEEEHH:mm, yyyy"格式,设置语言环境为英语。其它类似。
        if(temp.endsWith("1") && !temp.endsWith("11")){
            format = new SimpleDateFormat("dd'st' MMMM yyyy, EEEE HH:mm", Locale.ENGLISH);
        }else if(temp.endsWith("2") && !temp.endsWith("12")){
            format = new SimpleDateFormat("dd'nd' MMMM yyyy, EEEE HH:mm",Locale.ENGLISH);
        }else if(temp.endsWith("3") && !temp.endsWith("13")){
            format = new SimpleDateFormat("dd'rd' MMMM yyyy, EEEE HH:mm",Locale.ENGLISH);
        }else{
            format = new SimpleDateFormat("dd'th' MMMM yyyy, EEEE HH:mm",Locale.ENGLISH);
        }
        return format.format(d);
    }

    public Integer getWeatherImgRes(Integer code){
        Integer[] resources = {
                R.drawable.icon0,
                R.drawable.icon1,
                R.drawable.icon2,
                R.drawable.icon3,
                R.drawable.icon4,
                R.drawable.icon5,
                R.drawable.icon6,
                R.drawable.icon7,
                R.drawable.icon8,
                R.drawable.icon9,
                R.drawable.icon10,
                R.drawable.icon11,
                R.drawable.icon12,
                R.drawable.icon13,
                R.drawable.icon14,
                R.drawable.icon15,
                R.drawable.icon16,
                R.drawable.icon17,
                R.drawable.icon18,
                R.drawable.icon19,
                R.drawable.icon20,
                R.drawable.icon21,
                R.drawable.icon22,
                R.drawable.icon23,
                R.drawable.icon24,
                R.drawable.icon25,
                R.drawable.icon26,
                R.drawable.icon27,
                R.drawable.icon28,
                R.drawable.icon29,
                R.drawable.icon30,
                R.drawable.icon31,
                R.drawable.icon32,
                R.drawable.icon33,
                R.drawable.icon34,
                R.drawable.icon35,
                R.drawable.icon36,
                R.drawable.icon37,
                R.drawable.icon38,
                R.drawable.icon39,
                R.drawable.icon40,
                R.drawable.icon41,
                R.drawable.icon42,
                R.drawable.icon43,
                R.drawable.icon44,
                R.drawable.icon45,
                R.drawable.icon46,
                R.drawable.icon47
        };
        if(code == null || code < 0 || code > 47)
            return R.drawable.icon_wet;
        return resources[code];
    }
}
