package edu.rhit.groupalarm.groupalarm;

import android.app.PendingIntent;

import com.google.firebase.database.Exclude;

import java.sql.Time;

public class Alarm {
    private int mHour;
    private int mMinute;
    private String key;
    private String ownerId;
    private boolean mOpen;
    private boolean mVisible;
    private boolean mRinging;
    private PendingIntent mPendingIntent;

    public Alarm(int hour, int minute, String uid) {
        mHour = hour;
        mMinute = minute;
        mOpen = true;
        mVisible = false;
        mRinging = false;
        mPendingIntent = null;
        ownerId = uid;
    }

    public Alarm() {

    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getmHour() {
        return mHour;
    }

    public void setmHour(int mHour) {
        this.mHour = mHour;
    }

    public int getmMinute() {
        return mMinute;
    }

    public void setmMinute(int mMinute) {
        this.mMinute = mMinute;
    }

    public boolean ismOpen() {
        return mOpen;
    }

    public void setmOpen(boolean mOpen) {
        this.mOpen = mOpen;
    }

    public boolean ismVisible() {
        return mVisible;
    }

    public void setmVisible(boolean mVisible) {
        this.mVisible = mVisible;
    }

    public boolean ismRinging() {
        return mRinging;
    }

    public void setmRinging(boolean mRinging) {
        this.mRinging = mRinging;
    }

    @Exclude
    public PendingIntent getmPendingIntent() {
        return mPendingIntent;
    }

    public void setmPendingIntent(PendingIntent mPendingIntent) {
        this.mPendingIntent = mPendingIntent;
    }
}
