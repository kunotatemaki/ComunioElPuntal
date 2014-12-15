package com.rukiasoft.androidapps.comunioelpuntal;

// Do not modify 

public class NotificationItem {

    private int mID;
    private String mDateSent = "";
    private String mTimeSent = "";
    private String mMessage = "";
    private String mShortMessage = "";
    private boolean mRead;

    public NotificationItem(int id, String sent, String shortMessage, String message, boolean read) {

        String[] separated = sent.split(" ");
        if (separated.length == 2) {
            mDateSent = separated[0].trim(); // this will contain Date
            mTimeSent = separated[1].trim(); // this will contain Time
        }

        this.mShortMessage = shortMessage;
        this.mMessage = message;
        mRead = read;
        mID = id;
    }

    // Create a new ToDoItem from data packaged in an Intent

    public String getDateSent() {
        return mDateSent;
    }

    public void setDateSent(String DateSent) {
        mDateSent = DateSent;
    }

    public String getTimeSent() {
        return mTimeSent;
    }

    public void setTimeSent(String TimeSent) {
        mTimeSent = TimeSent;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getShortMessage() {
        return mShortMessage;
    }

    public void setShortMessage(String shortMessage) {
        mShortMessage = shortMessage;
    }

    public boolean isRead() {
        return mRead;
    }

    public void alreadyRead() {
        mRead = true;
    }

    public int getID() {
        return mID;
    }
}
