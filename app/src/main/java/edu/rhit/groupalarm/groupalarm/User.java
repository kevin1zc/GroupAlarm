package edu.rhit.groupalarm.groupalarm;

import java.util.ArrayList;

public class User {
    private String mUsername;
    private ArrayList<User> mFriendList;
    private ArrayList<Alarm> mAlarms;
    private int mVolume;
    private boolean mVibrate;
    private String mLanguage;
    private String mRingtone;

    public User(String username) {
        mUsername = username;
        mFriendList = new ArrayList<>();
        mAlarms = new ArrayList<>();
        //Default value or the last alarm
        mVolume = 50;
        mVibrate = false;
        mLanguage = "English";
        mRingtone = "default ringtone";
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public ArrayList<User> getmFriendList() {
        return mFriendList;
    }

    public void setmFriendList(ArrayList<User> mFriendList) {
        this.mFriendList = mFriendList;
    }

    public ArrayList<Alarm> getmAlarms() {
        return mAlarms;
    }

    public void setmAlarms(ArrayList<Alarm> mAlarms) {
        this.mAlarms = mAlarms;
    }

    public int getmVolume() {
        return mVolume;
    }

    public void setmVolume(int mVolume) {
        this.mVolume = mVolume;
    }

    public boolean ismVibrate() {
        return mVibrate;
    }

    public void setmVibrate(boolean mVibrate) {
        this.mVibrate = mVibrate;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public void setmLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    public String getmRingtone() {
        return mRingtone;
    }

    public void setmRingtone(String mRingtone) {
        this.mRingtone = mRingtone;
    }
}
