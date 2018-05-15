package edu.rhit.groupalarm.groupalarm.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import edu.rhit.groupalarm.groupalarm.Adapters.AlarmAdapter;
import edu.rhit.groupalarm.groupalarm.Alarm;
import edu.rhit.groupalarm.groupalarm.FriendsActivity;
import edu.rhit.groupalarm.groupalarm.MainActivity;
import edu.rhit.groupalarm.groupalarm.R;
import edu.rhit.groupalarm.groupalarm.SettingsActivity;
import edu.rhit.groupalarm.groupalarm.User;

/**
 * A placeholder fragment containing a simple view.
 */
public class AlarmFragment extends Fragment {
    public static final int MODE_PRIVATE = 0x0000;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String CURRENT_USER = "current_user";
    private static final String CurrentTab = "current_tab";
    private static final int RC_USER_SETTINGS = 1;
    private final static String PREFS = "PREFS";
    private static final int RC_FRIEND = 2;
    private static Context mContext;
    private static ViewPager mViewPager;
//    private static SharedPreferences prefs;
    private AlarmAdapter mAlarmAdapter;
    private User mUser;
    private DatabaseReference mCurrentUserRef;
    private LogoutListener logoutListener;
    private int tab;
//    private int currentTab;

    public AlarmFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AlarmFragment newInstance(int sectionNumber, User user, Context context, ViewPager viewPager) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable(CURRENT_USER, user);
        mContext = context;
        mViewPager = viewPager;
//        prefs = mContext.getSharedPreferences(PREFS, MODE_PRIVATE);
        fragment.setArguments(args);
        return fragment;
    }

//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS, MODE_PRIVATE);
////        currentTab = prefs.getInt(CurrentTab,0);
////        tab=currentTab;
////        mViewPager.setCurrentItem(currentTab);
////        if (savedInstanceState!=null){
////            currentTab=savedInstanceState.getInt(CurrentTab);
////        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS, MODE_PRIVATE);
        tab = getArguments().getInt(ARG_SECTION_NUMBER) - 1;
//        Log.d("aaaaaaaaaaaaaaaa", prefs.getInt(CurrentTab, tab) + "onCreateView");
//        currentTab = prefs.getInt(CurrentTab, tab);
//        tab = currentTab;

        mUser = getArguments().getParcelable(CURRENT_USER);
        mCurrentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getmUid());
        View rootView;
        switch (tab) {
            case 0: //My Alarm
                rootView = inflater.inflate(R.layout.fragment_my_alarm, container, false);

                FloatingActionButton fab = rootView.findViewById(R.id.add_fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addAlarm();
                    }
                });

                RecyclerView recyclerView = rootView.findViewById(R.id.recycler_my_alarm);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerView.setHasFixedSize(true);
                mAlarmAdapter = new AlarmAdapter(mUser, mContext, recyclerView);

                recyclerView.setAdapter(mAlarmAdapter);
                Button awakeButton = rootView.findViewById(R.id.button_awaken);
                awakeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mUser.setmIsAwake(true);
                        mCurrentUserRef.child("mIsAwake").setValue(true);
                    }
                });
                break;

            case 1: //Personal
                rootView = inflater.inflate(R.layout.fragment_personal, container, false);
                final ImageView statusView = rootView.findViewById(R.id.status_imageview);
                mCurrentUserRef.child("mIsAwake").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean awake = dataSnapshot.getValue(boolean.class);
                        if (awake) {
                            statusView.setColorFilter(ContextCompat.getColor(mContext, R.color.green));
                        } else {
                            statusView.setColorFilter(ContextCompat.getColor(mContext, R.color.red));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                TextView usernameView = rootView.findViewById(R.id.username_textview);
                usernameView.setText(mUser.getmUsername());
                LinearLayout settingsView = rootView.findViewById(R.id.settings_view);
                settingsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SettingsActivity.class);
                        intent.putExtra(MainActivity.EXTRA_USER, mUser);
//                        startActivity(intent);
                        startActivityForResult(intent, RC_USER_SETTINGS);
                    }
                });
                LinearLayout friendsView = rootView.findViewById(R.id.friends_view);
                friendsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FriendsActivity.class);
                        startActivityForResult(intent, RC_FRIEND);
                    }
                });
                LinearLayout logoutView = rootView.findViewById(R.id.logout_view);
                logoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logoutListener.logout();
                    }
                });
                break;

            case 2: //Friends Alarm
                rootView = inflater.inflate(R.layout.fragment_friends_alarm, container, false);
                break;

            default:
                rootView = null;
                break;
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LogoutListener) {
            logoutListener = (LogoutListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LogoutListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logoutListener = null;
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        prefs.edit().clear();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        currentTab = mViewPager.getCurrentItem();
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt(CurrentTab, currentTab);
//        editor.commit();
//        Log.d("aaaaaaaaaaa", "onDestroy" + currentTab);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("aaaaaaaaaaaaa", "onResume" + currentTab);
//    }

    private void addAlarm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.set_time);
        View view = getLayoutInflater().inflate(R.layout.add_alarm, null, false);
        TimePicker mTimePicker = view.findViewById(R.id.timepicker);
        DateFormat df = new SimpleDateFormat("HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        String[] time = date.split(":");
        int hr = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[1]);
        final int[] hourAndMinute = {hr, min};
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hourAndMinute[0] = hourOfDay;
                hourAndMinute[1] = minute;
            }
        });

        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mUser.setmIsAwake(false);
                boolean hasDuplicate = false;
                ArrayList<Alarm> currentAlarms = mAlarmAdapter.getmAlarmList();
                for (int i = 0; i < currentAlarms.size(); i++) {
                    if (hourAndMinute[0] == currentAlarms.get(i).getmHour() && hourAndMinute[1] == currentAlarms.get(i).getmMinute()) {
                        hasDuplicate = true;
                        currentAlarms.get(i).setmOpen(true);
                        break;
                    }
                }
                if (!hasDuplicate) {
                    Alarm currentAlarm = new Alarm(hourAndMinute[0], hourAndMinute[1], mUser.getmUid());
                    String hr = hourAndMinute[0] < 10 ? "0" + hourAndMinute[0] : "" + hourAndMinute[0];
                    String min = hourAndMinute[1] < 10 ? "0" + hourAndMinute[1] : "" + hourAndMinute[1];
                    int id = Integer.parseInt(String.format("%s%s", hr, min));
                    currentAlarm.setAlarmID(id);
                    currentAlarm.setmStringHour(String.format("%s", hr));
                    currentAlarm.setmStringMinute(String.format("%s", min));
                    currentAlarm.setmKey(currentAlarm.getOwnerId() + currentAlarm.getmStringHour() + currentAlarm.getmStringMinute());
                    mAlarmAdapter.addAlarm(currentAlarm);
                    mUser.setmIsAwake(false);
                    mCurrentUserRef.child("mIsAwake").setValue(false);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_USER_SETTINGS) {
                mUser = data.getParcelableExtra(MainActivity.EXTRA_USER);
            }
            mViewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewPager.setCurrentItem(1);
                    Log.d("aaaaaaaaaaaaa", "jkavnoanvowjaonvok");
                }
            }, 200);
        }
    }

//    public void onSaveInstanceState(Bundle outState) {
//        outState.putInt(CurrentTab, currentTab);
//    }

    public interface LogoutListener {
        void logout();
    }
}