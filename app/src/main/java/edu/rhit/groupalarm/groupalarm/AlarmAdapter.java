package edu.rhit.groupalarm.groupalarm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    private User mUser;
    private Context mContext;
    private RecyclerView mRecyclerView;

    public AlarmAdapter(User user, Context context, RecyclerView recyclerView) {
        mUser = user;
        mContext = context;
        mRecyclerView = recyclerView;
    }

    public void addAlarm(int hour, int minute) {
        this.mUser.getmAlarms().add(new Alarm(hour, minute));
        notifyItemInserted(mUser.getmAlarms().size() - 1);
        mRecyclerView.scrollToPosition(mUser.getmAlarms().size() - 1);
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        final Alarm currentAlarm=mUser.getmAlarms().get(position);
        holder.mAlarmTime.setText(currentAlarm.getmHour() + ":" + currentAlarm.getmMinute());
        holder.mActivateCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAlarm.setmOpen(!currentAlarm.ismOpen());
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
        return mUser.getmAlarms().size();
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        private TextView mAlarmTime;
        private CheckBox mActivateCheckBox;
        private CheckBox mVisibleCheckBox;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            mAlarmTime = itemView.findViewById(R.id.text_my_alarm_time);
            mActivateCheckBox = itemView.findViewById(R.id.checkbox_activated);
            mVisibleCheckBox = itemView.findViewById(R.id.checkbox_visible);
        }
    }
}
