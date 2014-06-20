package com.example.ulikeitweather.app.entity;

/**
 * stores name of a city and according longitude and latitude
 */

public class MyLocation {
    private double mLatitude;
    private double mLongitude;
    private String mCity;


    public double getLatitude() {
        return mLatitude;
    }


    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }


    public double getLongitude() {
        return mLongitude;
    }


    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }


    public String getCity() {
        return mCity;
    }


    public void setCity(String city) {
        this.mCity = city;
    }
}
