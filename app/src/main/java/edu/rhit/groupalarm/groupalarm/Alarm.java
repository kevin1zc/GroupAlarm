package edu.rhit.groupalarm.groupalarm;

import java.sql.Time;

public class Alarm {
    private Time mTime;
    private boolean mOpen;
    private boolean mVisible;
    private boolean mRinging;

    public Alarm(Time time) {
        mTime = time;
        mOpen = true;
        mVisible = false;
        mRinging = false;
    }

    public Time getmTime() {
        return mTime;
    }

    public void setmTime(Time mTime) {
        this.mTime = mTime;
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
