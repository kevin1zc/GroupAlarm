package edu.rhit.groupalarm.groupalarm.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import edu.rhit.groupalarm.groupalarm.Alarm;
import edu.rhit.groupalarm.groupalarm.R;
import edu.rhit.groupalarm.groupalarm.User;

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
        this.mUser.getmAlarms().remove(positon);
        notifyItemRemoved(positon);
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        final Alarm currentAlarm = mUser.getmAlarms().get(position);
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


}