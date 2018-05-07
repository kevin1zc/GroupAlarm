package edu.rhit.groupalarm.groupalarm;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Alarm implements Parcelable {

    private int mHour;
    private int mMinute;
    private String mKey;
    private String mStringHour;
    private String mStringMinute;
    private String ownerId;
    private boolean mOpen;
    private boolean mVisible;
    private boolean mRinging;
    private int alarmID;

    public Alarm(int hour, int minute, String uid) {
        mHour = hour;
        mMinute = minute;
        mOpen = true;
        mVisible = false;
        mRinging = false;
        ownerId = uid;
    }


    public Alarm() {

    }

    protected Alarm(Parcel in) {
        mHour = in.readInt();
        mMinute = in.readInt();
        mKey = in.readString();
        mStringHour = in.readString();
        mStringMinute = in.readString();
        ownerId = in.readString();
        mOpen = in.readByte() != 0;
        mVisible = in.readByte() != 0;
        mRinging = in.readByte() != 0;
        alarmID = in.readInt();
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

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getmStringHour() {
        return mStringHour;
    }

    public void setmStringHour(String mStringHour) {
        this.mStringHour = mStringHour;
    }

    public String getmStringMinute() {
        return mStringMinute;
    }

    public void setmStringMinute(String mStringMinute) {
        this.mStringMinute = mStringMinute;
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

    public int getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(int alarmID) {
        this.alarmID = alarmID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mHour);
        dest.writeInt(mMinute);
        dest.writeString(mKey);
        dest.writeString(mStringHour);
        dest.writeString(mStringMinute);
        dest.writeString(ownerId);
        dest.writeByte((byte) (mOpen ? 1 : 0));
        dest.writeByte((byte) (mVisible ? 1 : 0));
        dest.writeByte((byte) (mRinging ? 1 : 0));
        dest.writeInt(alarmID);
    }
}
