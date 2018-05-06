package edu.rhit.groupalarm.groupalarm;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class User implements Parcelable {

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
    private String mUsername;
    private ArrayList<User> mFriendList;
    private ArrayList<Alarm> mAlarms;
    private int mVolume;
    private boolean mVibrate;
    private String mLanguage;
    private String mRingtoneLocation;
    private boolean mIsAwake;
    private Context mContext;
    private String mUid;

    public User(String username, String uid, Context context) {
        mUsername = username;
        mUid = uid;
        mContext = context;
        mFriendList = new ArrayList<>();
        mAlarms = new ArrayList<>();
        mVolume = ((AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE)).getStreamMaxVolume(AudioManager.STREAM_ALARM);
        mVibrate = false;
        mLanguage = "English";
        mRingtoneLocation = "placeholder";
        mIsAwake = true;
    }


    public User() {

    }

    protected User(Parcel in) {
        mUsername = in.readString();
        mFriendList = in.createTypedArrayList(User.CREATOR);
        mAlarms = in.createTypedArrayList(Alarm.CREATOR);
        mVolume = in.readInt();
        mVibrate = in.readByte() != 0;
        mLanguage = in.readString();
        mRingtoneLocation = in.readString();
        mIsAwake = in.readByte() != 0;
        mUid = in.readString();
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
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

    public String getmRingtoneLocation() {
        return mRingtoneLocation;
    }

    public void setmRingtoneLocation(String mRingtoneLocation) {
        this.mRingtoneLocation = mRingtoneLocation;
    }

    public boolean ismIsAwake() {
        return mIsAwake;
    }

    public void setmIsAwake(boolean mIsAwake) {
        this.mIsAwake = mIsAwake;
    }

    @Exclude
    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUsername);
        dest.writeTypedList(mFriendList);
        dest.writeTypedList(mAlarms);
        dest.writeInt(mVolume);
        dest.writeByte((byte) (mVibrate ? 1 : 0));
        dest.writeString(mLanguage);
        dest.writeString(mRingtoneLocation);
        dest.writeByte((byte) (mIsAwake ? 1 : 0));
        dest.writeString(mUid);
    }
}
