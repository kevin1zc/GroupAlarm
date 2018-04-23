package edu.rhit.groupalarm.groupalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.util.Log;

public class PendingIntentBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("aaaaaaaaaaaaa","gfjkdsjdskjfvnvnjnjvv");
        byte[] byteArrayData=intent.getByteArrayExtra(MainActivity.EXTRA_USER);
        Parcel parcel=Parcel.obtain();
        parcel.unmarshall(byteArrayData,0,byteArrayData.length);
        parcel.setDataPosition(0);
        User mUser=User.CREATOR.createFromParcel(parcel);
//        User mUser = intent.getParcelableExtra(MainActivity.EXTRA_USER);
        Log.d("aaaaaaaaaaaaa",mUser.getmUsername());
        Intent newIntent = new Intent(context, AlarmRingActivity.class);
        newIntent.putExtra(MainActivity.EXTRA_USER, mUser);
        context.startActivity(newIntent);
    }
}
