package edu.rhit.groupalarm.groupalarm.Adapters;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import edu.rhit.groupalarm.groupalarm.Alarm;
import edu.rhit.groupalarm.groupalarm.MainActivity;
import edu.rhit.groupalarm.groupalarm.PendingIntentBroadCastReceiver;
import edu.rhit.groupalarm.groupalarm.R;
import edu.rhit.groupalarm.groupalarm.User;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    private User mUser;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private DatabaseReference mAlarmRef;
    private ArrayList<Alarm> mAlarmList;
    private Query myAlarmRef;

    public AlarmAdapter(User user, Context context, RecyclerView recyclerView) {
        mUser = user;
        mContext = context;
        mRecyclerView = recyclerView;
        mAlarmList = new ArrayList<>();
        mAlarmRef = FirebaseDatabase.getInstance().getReference().child("alarms");
//        mAlarmRef.addChildEventListener(new AlarmChildEventListener());
        myAlarmRef = mAlarmRef.orderByChild("ownerId").equalTo(mUser.getmUid());
        myAlarmRef.addChildEventListener(new AlarmChildEventListener());
        notifyDataSetChanged();
    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    public DatabaseReference getmAlarmRef() {
        return mAlarmRef;
    }

    public void setmAlarmRef(DatabaseReference mAlarmRef) {
        this.mAlarmRef = mAlarmRef;
    }

    public ArrayList<Alarm> getmAlarmList() {
        return mAlarmList;
    }

    public void setmAlarmList(ArrayList<Alarm> mAlarmList) {
        this.mAlarmList = mAlarmList;
    }

    public void addAlarm(Alarm alarm) {
        mAlarmRef.child(alarm.getmKey()).setValue(alarm);
    }

    private void removeAlarmDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.delete_alarm_dialog));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeAlarm(position);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void removeAlarm(int position) {
        mAlarmRef.child(mAlarmList.get(position).getmKey()).removeValue();
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        final Alarm currentAlarm = mAlarmList.get(position);
        holder.mAlarmTime.setText(currentAlarm.getmStringHour() + ":" + currentAlarm.getmStringMinute());
        if (currentAlarm.ismOpen()) {
            holder.mActivateCheckBox.setChecked(true);
        } else {
            holder.mActivateCheckBox.setChecked(false);
        }
        if (currentAlarm.ismVisible()) {
            holder.mVisibleCheckBox.setChecked(true);
        } else {
            holder.mVisibleCheckBox.setChecked(false);
        }
        holder.mActivateCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean open = !currentAlarm.ismOpen();
                currentAlarm.setmOpen(open);
                mAlarmRef.child(currentAlarm.getmKey()).child("mOpen").setValue(open);
                if (!currentAlarm.ismOpen()) {
                    cancelAlarm(currentAlarm);
                } else {
                    mUser.setmIsAwake(false);
                    FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getmUid()).child("mIsAwake").setValue(false);
                    activateAlarm(currentAlarm);
                }
            }
        });
        holder.mVisibleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean visible = !currentAlarm.ismVisible();
                currentAlarm.setmVisible(visible);
                mAlarmRef.child(currentAlarm.getmKey()).child("mVisible").setValue(visible);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlarmList.size();
    }

    private void activateAlarm(Alarm alarm) {
        Intent intent = new Intent(mContext, PendingIntentBroadCastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.ALARM, alarm);
        intent.putExtra(MainActivity.ALARM, bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, alarm.getAlarmID(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getmHour());
        calendar.set(Calendar.MINUTE, alarm.getmMinute());
        calendar.set(Calendar.SECOND, 0);
        if(calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        Log.d("ALARMTAG", calendar.getTimeInMillis() + "");
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(Alarm alarm) {
        Intent intent = new Intent(mContext, PendingIntentBroadCastReceiver.class);
        intent.putExtra(MainActivity.ALARM, alarm);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, alarm.getAlarmID(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        private TextView mAlarmTime;
        private CheckBox mActivateCheckBox;
        private CheckBox mVisibleCheckBox;

        public AlarmViewHolder(final View itemView) {
            super(itemView);
            mAlarmTime = itemView.findViewById(R.id.text_my_alarm_time);
            mActivateCheckBox = itemView.findViewById(R.id.checkbox_activated);
            mVisibleCheckBox = itemView.findViewById(R.id.checkbox_visible);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    removeAlarmDialog(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    private class AlarmChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Alarm alarm = dataSnapshot.getValue(Alarm.class);
            mAlarmList.add(alarm);
            Collections.sort(mAlarmList);
            notifyDataSetChanged();
            if (alarm.ismOpen()) {
                activateAlarm(alarm);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String keyToDelete = dataSnapshot.getKey();
            for (Alarm alarm : mAlarmList) {
                if (keyToDelete.equals(alarm.getmKey())) {
                    mAlarmList.remove(alarm);
                    notifyDataSetChanged();
                    cancelAlarm(alarm);
                    return;
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}