package edu.rhit.groupalarm.groupalarm.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.rhit.groupalarm.groupalarm.Adapters.AlarmPagerAdapter;
import edu.rhit.groupalarm.groupalarm.MainActivity;
import edu.rhit.groupalarm.groupalarm.R;
import edu.rhit.groupalarm.groupalarm.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    private User mUser;
    private ViewPager mViewPager;
    private AlarmPagerAdapter mAlarmPagerAdapter;
    private String uid;
    private String username;
    private DatabaseReference mUserRef;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String username, String uid) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.USER, username);
        args.putString(MainActivity.UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        uid = getArguments().getString(MainActivity.UID);
        username = getArguments().getString(MainActivity.USER);

        mUser = MainActivity.getUserInstance();


//        mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
//        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User currentUser = dataSnapshot.getValue(User.class);
//                Log.d("aaaaaaaaaaaaaa", currentUser.ismVibrate()+"has");
//                if (currentUser != null) {
//                    Log.d("aaaaaaaaaaaaaa", "has");
//
//                    mUser = currentUser;
//                } else {
//                    Log.d("aaaaaaaaaaaaaa", "no");
//                    mUser = new User(username, uid, getContext());
//                    mUserRef.setValue(mUser);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("aaaaaaaaaaaaaa", "cancel");
//            }
//        });
//        mUser = new User(username, uid, getContext());
        mUserRef.setValue(mUser);

        mViewPager = view.findViewById(R.id.container);
        Log.d("aaaaaaaaaaaaaa", mUser.ismVibrate()+"gtsrggsvfvdfbbgdv");
        mAlarmPagerAdapter = new AlarmPagerAdapter(getChildFragmentManager(), mUser);
        mViewPager.setAdapter(mAlarmPagerAdapter);
//        mViewPager.setCurrentItem(1);
        mListener.OnFragmentCreated(this);
        return view;
    }

    public ViewPager getmViewPager() {
        return mViewPager;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void OnFragmentCreated(MainFragment fragment);
    }
}