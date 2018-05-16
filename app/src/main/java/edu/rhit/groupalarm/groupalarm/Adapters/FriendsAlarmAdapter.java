package edu.rhit.groupalarm.groupalarm.Adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import edu.rhit.groupalarm.groupalarm.Alarm;
import edu.rhit.groupalarm.groupalarm.Fragments.MainFragment;
import edu.rhit.groupalarm.groupalarm.R;
import edu.rhit.groupalarm.groupalarm.User;

public class FriendsAlarmAdapter extends RecyclerView.Adapter<FriendsAlarmAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Alarm> alarmList;
    private HashMap<Alarm, User> alarmUserMap;
    private DatabaseReference alarmRef = FirebaseDatabase.getInstance().getReference().child("alarms");

    public FriendsAlarmAdapter(Context context) {
        this.context = context;
        alarmList = new ArrayList<>();
        alarmUserMap = new HashMap<>();
        startTask();
    }

    private void startTask() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    loadData();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();
    }

    private void loadData() {
        alarmList.clear();
        alarmUserMap.clear();
        User me = MainFragment.getCurrentUserInstance();
        for (String friendUid : me.getmFriendList().keySet()) {
            if (friendUid.equals(me.getmUid())) {
                continue;
            }
            Query query = alarmRef.orderByChild("ownerId").equalTo(friendUid);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot alarmSnapshot : dataSnapshot.getChildren()) {
                        Alarm alarm = alarmSnapshot.getValue(Alarm.class);
                        if (alarm.ismVisible()) {
                            alarmList.add(alarm);
                            alarmUserMap.put(alarm, null);
                        }
                    }
                    getUser();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void getUser() {
        for (final Alarm alarm : alarmUserMap.keySet()) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(alarm.getOwnerId());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    alarmUserMap.put(alarm, user);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_alarm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);
        User user = alarmUserMap.get(alarm);

        if (user.ismIsAwake()) {
            holder.isAwaken.setColorFilter(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.isAwaken.setColorFilter(ContextCompat.getColor(context, R.color.red));
        }
        holder.nameTextView.setText(user.getmUsername());
        String time = alarm.getmStringHour() + ":" + alarm.getmStringMinute();
        holder.timeTextView.setText(time);
        if (alarm.ismRinging()) {
            holder.isRinging.setVisibility(View.VISIBLE);
        } else {
            holder.isRinging.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return alarmUserMap.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView isAwaken;
        private TextView nameTextView;
        private TextView timeTextView;
        private ImageView isRinging;


        public ViewHolder(View itemView) {
            super(itemView);
            isAwaken = itemView.findViewById(R.id.img_awake);
            nameTextView = itemView.findViewById(R.id.text_friend_name);
            timeTextView = itemView.findViewById(R.id.text_time_friend_alarm);
            isRinging = itemView.findViewById(R.id.img_ringing);
        }
    }

}
