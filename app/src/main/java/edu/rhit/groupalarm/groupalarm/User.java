package edu.rhit.groupalarm.groupalarm;

import android.content.Context;
import android.media.AudioManager;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

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
    private int mVolume;
    private boolean mVibrate;
    private String mRingtoneLocation;
    private boolean mIsAwake;
    private String mUid;

    private HashMap<String, Boolean> mFriendList;

    public User() {

    }

    public User(String username, String uid, Context context) {
        mUsername = username;
        mUid = uid;
        mFriendList = new HashMap<>();
        mVolume = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).getStreamMaxVolume(AudioManager.STREAM_ALARM);
        mVibrate = false;
        mRingtoneLocation = "Default Ringtone/Tanaki Alison - One Wish.mp3";
        mIsAwake = true;
        mFriendList.put(mUid, true);
    }

    public User(HashMap<String, Boolean> friendList, boolean isAwake, String ringtoneLocation, String uid, String username, int volume, boolean vibrate) {
        mUsername = username;
        mUid = uid;
        mFriendList = friendList;
        mVolume = volume;
        mVibrate = vibrate;
        mRingtoneLocation = ringtoneLocation;
        mIsAwake = isAwake;
    }

    protected User(Parcel in) {
        mUsername = in.readString();
        mVolume = in.readInt();
        mVibrate = in.readByte() != 0;
        mRingtoneLocation = in.readString();
        mIsAwake = in.readByte() != 0;
        mUid = in.readString();
        mFriendList = new HashMap<>();
        in.readMap(mFriendList, Boolean.class.getClassLoader());
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

    public HashMap<String, Boolean> getmFriendList() {
        return mFriendList;
    }

    public void setmFriendList(HashMap<String, Boolean> mFriendList) {
        this.mFriendList = mFriendList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUsername);
        dest.writeInt(mVolume);
        dest.writeByte((byte) (mVibrate ? 1 : 0));
        dest.writeString(mRingtoneLocation);
        dest.writeByte((byte) (mIsAwake ? 1 : 0));
        dest.writeString(mUid);
        dest.writeMap(mFriendList);
    }
}
