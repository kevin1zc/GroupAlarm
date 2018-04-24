package edu.rhit.groupalarm.groupalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.util.Log;

public class PendingIntentBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        byte[] byteArrayData = intent.getByteArrayExtra(MainActivity.EXTRA_USER);
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(byteArrayData, 0, byteArrayData.length);
        parcel.setDataPosition(0);
        User mUser = User.CREATOR.createFromParcel(parcel);
        Log.d("aaaaaa",mUser.ismVibrate()+"");
        Intent newIntent = new Intent(context, AlarmRingActivity.class);
        newIntent.putExtra(MainActivity.EXTRA_USER, mUser);
        context.startActivity(newIntent);
    }
}
