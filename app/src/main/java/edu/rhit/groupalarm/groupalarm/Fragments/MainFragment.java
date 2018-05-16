package edu.rhit.groupalarm.groupalarm.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    private static User mUser;
    private OnFragmentInteractionListener mListener;
    private ViewPager mViewPager;
    private AlarmPagerAdapter mAlarmPagerAdapter;
    private String uid;
    private String username;
    private DatabaseReference mUserRef;
    private Context mContext;
    private FragmentManager childFragmentManager;

    private boolean fragmentShowUp;

    public MainFragment() {
        // Required empty public constructor
    }

    public static User getCurrentUserInstance() {
        return mUser;
    }

    public static MainFragment newInstance(String username, String uid) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.USER, username);
        args.putString(MainActivity.UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    public void getmContext(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childFragmentManager = getChildFragmentManager();
        Log.d("aaaaaaaaaa", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentShowUp = true;
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        uid = getArguments().getString(MainActivity.UID);
        username = getArguments().getString(MainActivity.USER);
        mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                if (currentUser == null) {
                    mUser = new User(username, uid, getContext());
                    mUserRef.setValue(mUser);
                } else {
                    mUser = currentUser;
                }
                if (fragmentShowUp) {
                    mViewPager = view.findViewById(R.id.container);
                    runOther();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void runOther() {
        mAlarmPagerAdapter = new AlarmPagerAdapter(childFragmentManager, mUser, mContext, mViewPager);
        mViewPager.setAdapter(mAlarmPagerAdapter);
        //TODO
        //java.lang.NullPointerException: Attempt to invoke interface method 'void edu.rhit.groupalarm.groupalarm.Fragments.MainFragment$OnFragmentInteractionListener.OnFragmentCreated(edu.rhit.groupalarm.groupalarm.Fragments.MainFragment)' on a null object reference
        mListener.OnFragmentCreated(this);
    }

    public ViewPager getmViewPager() {
        return mViewPager;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            Log.d("aaaaaaa", "true");
        } else {
            Log.d("aaaaaaa", "false");
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentShowUp = false;
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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