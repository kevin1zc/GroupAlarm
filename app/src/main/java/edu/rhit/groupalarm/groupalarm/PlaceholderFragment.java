package edu.rhit.groupalarm.groupalarm;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
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
            FloatingActionButton fab = rootView.findViewById(R.id.fab);
//                fab.hide();
            //fab.setVisibility(View.GONE);
        } else if (tab == 2) {
            rootView = inflater.inflate(R.layout.fragment_my_alarm, container, false);

//            RecyclerView recyclerView = rootView.findViewById(R.id.recycler_my_alarm);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            recyclerView.setHasFixedSize(true);
//            mAlarmAdapter = new AlarmAdapter(mUser,this,recyclerView);
//            recyclerView.setAdapter(mAlarmAdapter);
        } else {
            rootView = inflater.inflate(R.layout.fragment_friends_alarm, container, false);
            FloatingActionButton fab = rootView.findViewById(R.id.fab);
//                fab.hide();
            //fab.setVisibility(View.GONE);
        }
        return rootView;
    }
}
