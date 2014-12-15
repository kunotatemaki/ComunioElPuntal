package com.rukiasoft.androidapps.comunioelpuntal;

import android.content.Intent;


public class MainItem {

    private String mTitle = "";
    private int mPicture;
    private Intent mIntent;

    MainItem(String title, int picture, Intent intent) {
        this.mTitle = title;
        this.mPicture = picture;
        this.mIntent = intent;
    }

    // Create a new ToDoItem from data packaged in an Intent

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getPicture() {
        return mPicture;
    }

    public void setPicture(int picture) {
        mPicture = picture;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent intent) {
        mIntent = intent;
    }
}
