package edu.rhit.groupalarm.groupalarm;

import java.sql.Time;

public class Alarm {
    private int mHour;
    private int mMinute;
    private boolean mOpen;
    private boolean mVisible;
    private boolean mRinging;

    public Alarm(int hour, int minute) {
        mHour = hour;
        mMinute = minute;
        mOpen = true;
        mVisible = false;
        mRinging = false;
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
}
