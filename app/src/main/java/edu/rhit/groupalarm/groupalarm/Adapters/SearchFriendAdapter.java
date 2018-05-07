package edu.rhit.groupalarm.groupalarm.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import edu.rhit.groupalarm.groupalarm.User;

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.SearchFriendViewHolder> {
    private ArrayList<User> searchedUsers;
    private DatabaseReference mUserRef;
    private Query mSearchRef;

    public SearchFriendAdapter() {
        searchedUsers = new ArrayList<>();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @NonNull
    @Override
    public SearchFriendAdapter.SearchFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFriendAdapter.SearchFriendViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SearchFriendViewHolder extends RecyclerView.ViewHolder {
        public SearchFriendViewHolder(View itemView) {
            super(itemView);
        }
    }
}
