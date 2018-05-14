package edu.rhit.groupalarm.groupalarm.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import edu.rhit.groupalarm.groupalarm.Adapters.FriendsAdapter;
import edu.rhit.groupalarm.groupalarm.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendRequestFragment extends Fragment {
    private EditText mInput;


    public FriendRequestFragment() {
        // Required empty public constructor
    }

    public static FriendRequestFragment newInstance() {
        FriendRequestFragment fragment = new FriendRequestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_request, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_friend_request);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FriendsAdapter adapter = new FriendsAdapter(0, getContext());
        recyclerView.setAdapter(adapter);
        return view;
    }

}
