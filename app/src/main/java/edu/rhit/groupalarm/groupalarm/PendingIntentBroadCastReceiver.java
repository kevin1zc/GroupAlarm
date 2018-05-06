package edu.rhit.groupalarm.groupalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;

public class PendingIntentBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        byte[] byteArrayData = intent.getByteArrayExtra(MainActivity.ALARM);
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(byteArrayData, 0, byteArrayData.length);
        parcel.setDataPosition(0);
        Alarm mAlarm = Alarm.CREATOR.createFromParcel(parcel);
        Intent newIntent = new Intent(context, AlarmRingActivity.class);
        newIntent.putExtra(MainActivity.ALARM, mAlarm);
        context.startActivity(newIntent);
    }
}
