package edu.rhit.groupalarm.groupalarm;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.sql.Time;

public class Alarm implements Parcelable{
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

    protected Alarm(Parcel in) {
        mHour = in.readInt();
        mMinute = in.readInt();
        key = in.readString();
        ownerId = in.readString();
        mOpen = in.readByte() != 0;
        mVisible = in.readByte() != 0;
        mRinging = in.readByte() != 0;
        mPendingIntent = in.readParcelable(PendingIntent.class.getClassLoader());
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mHour);
        dest.writeInt(mMinute);
        dest.writeString(key);
        dest.writeString(ownerId);
        dest.writeByte((byte) (mOpen ? 1 : 0));
        dest.writeByte((byte) (mVisible ? 1 : 0));
        dest.writeByte((byte) (mRinging ? 1 : 0));
        dest.writeParcelable(mPendingIntent, flags);
    }
}
