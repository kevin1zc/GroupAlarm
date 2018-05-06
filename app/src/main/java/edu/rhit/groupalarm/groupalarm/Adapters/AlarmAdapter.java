package edu.rhit.groupalarm.groupalarm.Adapters;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.Calendar;

import edu.rhit.groupalarm.groupalarm.Alarm;
import edu.rhit.groupalarm.groupalarm.AlarmRingActivity;
import edu.rhit.groupalarm.groupalarm.MainActivity;
import edu.rhit.groupalarm.groupalarm.R;
import edu.rhit.groupalarm.groupalarm.User;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    private User mUser;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private DatabaseReference mAlarmRef;
    private ArrayList<Alarm> mAlarmList;

    public AlarmAdapter(User user, Context context, RecyclerView recyclerView) {
        mUser = user;
        mContext = context;
        mRecyclerView = recyclerView;
        mAlarmRef = FirebaseDatabase.getInstance().getReference().child("alarms");
        mAlarmRef.addChildEventListener(new AlarmChildEventListener());
        mAlarmList = new ArrayList<>();
    }

    public User getmUser() {
        return mUser;
    }

    public void addAlarm(Alarm alarm) {
        mAlarmRef.push().setValue(alarm);
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

    private void removeAlarm(int positon) {
        this.mAlarmList.get(positon).getmPendingIntent().cancel();
        this.mAlarmList.remove(positon);
        notifyItemRemoved(positon);
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, final int position) {
        final Alarm currentAlarm = mAlarmList.get(position);
        holder.mAlarmTime.setText(currentAlarm.getmHour() + ":" + currentAlarm.getmMinute());
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
                currentAlarm.setmOpen(!currentAlarm.ismOpen());
                if (!currentAlarm.ismOpen()) {
                    mAlarmList.get(position).getmPendingIntent().cancel();
                } else {
                    mUser.setmIsAwake(false);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, mAlarmList.get(position).getmHour());
                    calendar.set(Calendar.MINUTE, mAlarmList.get(position).getmMinute());
                    calendar.set(Calendar.SECOND, 0);
                    Intent intent = new Intent(mContext, AlarmRingActivity.class);
                    intent.putExtra(MainActivity.EXTRA_USER, mUser);
                    currentAlarm.setmPendingIntent(PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT));
                    ((AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), currentAlarm.getmPendingIntent());

                }
            }
        });
        holder.mVisibleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAlarm.setmVisible(!currentAlarm.ismVisible());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlarmList.size();
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
            alarm.setKey(dataSnapshot.getKey());
            mAlarmList.add(alarm);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
