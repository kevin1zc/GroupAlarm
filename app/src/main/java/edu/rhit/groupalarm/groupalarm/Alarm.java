package edu.rhit.groupalarm.groupalarm;

import android.app.PendingIntent;

import java.sql.Time;

public class Alarm {
    private int mHour;
    private int mMinute;
    private boolean mOpen;
    private boolean mVisible;
    private boolean mRinging;
    private PendingIntent mPendingIntent;

    public Alarm(int hour, int minute) {
        mHour = hour;
        mMinute = minute;
        mOpen = true;
        mVisible = false;
        mRinging = false;
        mPendingIntent=null;
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

    public PendingIntent getmPendingIntent() {
        return mPendingIntent;
    }

    public void setmPendingIntent(PendingIntent mPendingIntent) {
        this.mPendingIntent = mPendingIntent;
    }
}
