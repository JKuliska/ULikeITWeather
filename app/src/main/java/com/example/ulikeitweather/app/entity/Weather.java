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


    public String getmDayOfWeek() { return mDayOfWeek; }


    public void setmDayOfWeek(String mDayOfWeek) { this.mDayOfWeek = mDayOfWeek; }


    public String getmTempC() { return mTempC; }


    public void setmTempC(String mTempC) {
        this.mTempC = mTempC;
    }


    public String getmTempF() {
        return mTempF;
    }


    public void setmTempF(String mTempF) {
        this.mTempF = mTempF;
    }


    public String getmPrecip() {
        return mPrecip;
    }


    public void setmPrecip(String mPrecip) {
        this.mPrecip = mPrecip;
    }


    public String getmHumidity() {
        return mHumidity;
    }


    public void setmHumidity(String mHumidity) {
        this.mHumidity = mHumidity;
    }


    public String getmPressure() {
        return mPressure;
    }


    public void setmPressure(String mPressure) {
        this.mPressure = mPressure;
    }


    public String getmWindSpeedKmh() {
        return mWindSpeedKmh;
    }


    public void setmWindSpeedKmh(String mWindSpeedKmh) {
        this.mWindSpeedKmh = mWindSpeedKmh;
    }


    public String getmWindSpeedMph() {
        return mWindSpeedMph;
    }


    public void setmWindSpeedMph(String mWindSpeedMph) {
        this.mWindSpeedMph = mWindSpeedMph;
    }


    public String getmWindDir() {
        return mWindDir;
    }


    public void setmWindDir(String mWindDir) {
        this.mWindDir = mWindDir;
    }


    public String getmDescription() {
        return mDescription;
    }


    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }


    public String getmImgUrl() {
        return mImgUrl;
    }


    public void setmImgUrl(String mImgUrl) {
        this.mImgUrl = mImgUrl;
    }
}

