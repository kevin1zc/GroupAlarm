package edu.rhit.groupalarm.groupalarm.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

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
    public static final String ADAPTER = "adapter";
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String CURRENT_USER = "current_user";
    private static final int RC_USER_SETTING = 1;
    private AlarmAdapter mAlarmAdapter;
    private User mUser;

    private LogoutListener logoutListener;

    public AlarmFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AlarmFragment newInstance(int sectionNumber, User user) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable(CURRENT_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int tab = getArguments().getInt(ARG_SECTION_NUMBER);
        mUser = getArguments().getParcelable(CURRENT_USER);
        View rootView;
        switch (tab) {
            case 1:
                rootView = inflater.inflate(R.layout.fragment_my_alarm, container, false);

                FloatingActionButton fab = rootView.findViewById(R.id.add_fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addAlarm();
                    }
                });

                RecyclerView recyclerView = rootView.findViewById(R.id.recycler_my_alarm);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
                mAlarmAdapter = new AlarmAdapter(mUser, getContext(), recyclerView);

                recyclerView.setAdapter(mAlarmAdapter);
                Button awakeButton = rootView.findViewById(R.id.button_awaken);
                awakeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mUser.setmIsAwake(true);
                    }
                });
                break;

            case 2:
                rootView = inflater.inflate(R.layout.fragment_personal, container, false);
                ImageView statusView = rootView.findViewById(R.id.status_imageview);
                if (mUser.ismIsAwake()) {
                    //TODO on value listener on the awake boolean to change color.
                    statusView.setColorFilter(getContext().getColor(R.color.green));
                } else {
                    statusView.setColorFilter(getContext().getColor(R.color.red));
                }
                TextView usernameView = rootView.findViewById(R.id.username_textview);
                usernameView.setText(mUser.getmUsername());
                LinearLayout settingsView = rootView.findViewById(R.id.settings_view);
                settingsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SettingsActivity.class);
                        intent.putExtra(MainActivity.EXTRA_USER, mUser);
//                        startActivityForResult(intent,RC_USER_SETTING);
                        startActivity(intent);
                    }
                });
                LinearLayout friendsView = rootView.findViewById(R.id.friends_view);
                friendsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FriendsActivity.class);
                        startActivity(intent);
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

            case 3:
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

    private void addAlarm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    public interface LogoutListener {
        void logout();
    }

}