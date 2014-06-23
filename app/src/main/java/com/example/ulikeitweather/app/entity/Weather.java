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


    public String getDayOfWeek() { return mDayOfWeek; }


    public void setDayOfWeek(String dayOfWeek) { this.mDayOfWeek = dayOfWeek; }


    public String getTempC() { return mTempC; }


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

