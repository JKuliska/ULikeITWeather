package com.example.ulikeitweather.app.entity;

/**
 * Created by Asus on 21.6.2014.
 */
public class DrawerItem {
    private int mIconResource;
    private String mTitle;

    public DrawerItem() {

    }

    public DrawerItem(String title, int iconRes) {
        setTitle(title);
        setIconResource(iconRes);
    }

    public int getIconResource() {
        return mIconResource;
    }

    public void setIconResource(int iconResource) {
        this.mIconResource = iconResource;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }




}
