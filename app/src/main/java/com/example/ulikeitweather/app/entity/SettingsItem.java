package com.example.ulikeitweather.app.entity;

/**
 * Created by Asus on 21.6.2014.
 */
public class SettingsItem {

    private String mTitleText;
    private String mValueText;

    public SettingsItem() {

    }

    public SettingsItem(String title, String value) {
        setTitleText(title);
        setValueText(value);
    }

    public String getValueText() {
        return mValueText;
    }

    public void setValueText(String valueText) {
        this.mValueText = valueText;
    }

    public String getTitleText() {
        return mTitleText;
    }

    public void setTitleText(String titleText) {
        this.mTitleText = titleText;
    }

}
