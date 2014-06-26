package com.example.ulikeitweather.app.entity;

/**
 * stores information about weather
 */

public class Weather {

    private String mDayOfWeek;
    private String mDescription;
    private String mImgUrl;
    private String mTempC;
    private String mTempF;
    private String mPrecip;
    private String mHumidity;
    private String mPressure;
    private String mWindSpeedKmh;
    private String mWindSpeedMph;
    private String mWindDir;

    public Weather() {

    }

    public Weather(Weather weather) {
        if (weather.getDayOfWeek() != null) mDayOfWeek = weather.getDayOfWeek();
        if (weather.getDescription() != null) mDescription = weather.getDescription();
        if (weather.getImgUrl() != null) mImgUrl = weather.getImgUrl();
        if (weather.getTempC() != null) mTempC = weather.getTempC();
        if (weather.getTempF() != null) mTempF = weather.getTempF();
        if (weather.getPrecip() != null) mPrecip = weather.getPrecip();
        if (weather.getHumidity() != null) mHumidity = weather.getHumidity();
        if (weather.getPressure() != null) mPressure = weather.getPressure();
        if (weather.getWindSpeedKmh() != null) mWindSpeedKmh = weather.getWindSpeedKmh();
        if (weather.getWindSpeedMph() != null) mWindSpeedMph = weather.getWindSpeedMph();
        if (weather.getWindDir() != null) mWindDir = weather.getWindDir();

    }

    public String getDayOfWeek() {
        return mDayOfWeek;
    }


    public void setDayOfWeek(String dayOfWeek) {
        this.mDayOfWeek = dayOfWeek;
    }


    public String getTempC() {
        return mTempC;
    }


    public void setTempC(String tempC) {
        this.mTempC = tempC;
    }


    public String getTempF() {
        return mTempF;
    }


    public void setTempF(String tempF) {
        this.mTempF = tempF;
    }


    public String getPrecip() {
        return mPrecip;
    }


    public void setPrecip(String precip) {
        this.mPrecip = precip;
    }


    public String getHumidity() {
        return mHumidity;
    }


    public void setHumidity(String humidity) {
        this.mHumidity = humidity;
    }


    public String getPressure() {
        return mPressure;
    }


    public void setPressure(String pressure) {
        this.mPressure = pressure;
    }


    public String getWindSpeedKmh() {
        return mWindSpeedKmh;
    }


    public void setWindSpeedKmh(String windSpeedKmh) {
        this.mWindSpeedKmh = windSpeedKmh;
    }


    public String getWindSpeedMph() {
        return mWindSpeedMph;
    }


    public void setWindSpeedMph(String windSpeedMph) {
        this.mWindSpeedMph = windSpeedMph;
    }


    public String getWindDir() {
        return mWindDir;
    }


    public void setWindDir(String windDir) {
        this.mWindDir = windDir;
    }


    public String getDescription() {
        return mDescription;
    }


    public void setDescription(String description) {
        this.mDescription = description;
    }


    public String getImgUrl() {
        return mImgUrl;
    }


    public void setImgUrl(String imgUrl) {
        this.mImgUrl = imgUrl;
    }
}

