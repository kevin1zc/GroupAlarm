package edu.rhit.groupalarm.groupalarm.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import edu.rhit.groupalarm.groupalarm.Adapters.AlarmAdapter;
import edu.rhit.groupalarm.groupalarm.R;
import edu.rhit.groupalarm.groupalarm.User;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String CURRENT_USER = "current_user";
    private AlarmAdapter mAlarmAdapter;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, User user) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable(CURRENT_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int tab = getArguments().getInt(ARG_SECTION_NUMBER);
        View rootView;
        if (tab == 1) {
            rootView = inflater.inflate(R.layout.fragment_personal, container, false);
            LinearLayout settingsView = rootView.findViewById(R.id.settings_view);
            settingsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.main_content, new SettingsFragment());
                    ft.addToBackStack("Main");
                    ft.commit();
                }
            });
        } else if (tab == 2) {
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
            User mUser = getArguments().getParcelable(CURRENT_USER);
            mAlarmAdapter = new AlarmAdapter(mUser, getContext(), recyclerView);
            recyclerView.setAdapter(mAlarmAdapter);
        } else {
            rootView = inflater.inflate(R.layout.fragment_friends_alarm, container, false);
        }
        return rootView;
    }

    private void addAlarm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.set_time);
        View view = getLayoutInflater().inflate(R.layout.add_alarm, null, false);
        TimePicker mTimePicker = view.findViewById(R.id.timepicker);
        final int[] hourAndMinute = {0, 0};
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
                mAlarmAdapter.addAlarm(hourAndMinute[0], hourAndMinute[1]);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }
}
