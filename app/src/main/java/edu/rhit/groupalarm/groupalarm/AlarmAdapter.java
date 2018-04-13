package edu.rhit.groupalarm.groupalarm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    private User mUser;
    private Context mContext;
    private RecyclerView mRecyclerView;

    public AlarmAdapter(User user, Context context, RecyclerView recyclerView) {
        mUser = user;
        mContext = context;
        mRecyclerView = recyclerView;
    }

    public void addAlarm(int hour,int minute) {
        this.mUser.getmAlarms().add(new Alarm(hour,minute));
        notifyItemInserted(mUser.getmAlarms().size()-1);
        mRecyclerView.scrollToPosition(mUser.getmAlarms().size()-1);
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        public AlarmViewHolder(View itemView) {
            super(itemView);
        }
    }
}
