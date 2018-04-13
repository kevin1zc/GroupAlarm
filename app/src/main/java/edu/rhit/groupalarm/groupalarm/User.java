package edu.rhit.groupalarm.groupalarm;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable{
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

    protected User(Parcel in) {
        mUsername = in.readString();
        mFriendList = in.createTypedArrayList(User.CREATOR);
        mVolume = in.readInt();
        mVibrate = in.readByte() != 0;
        mLanguage = in.readString();
        mRingtone = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUsername);
        dest.writeTypedList(mFriendList);
        dest.writeInt(mVolume);
        dest.writeByte((byte) (mVibrate ? 1 : 0));
        dest.writeString(mLanguage);
        dest.writeString(mRingtone);
    }
}
