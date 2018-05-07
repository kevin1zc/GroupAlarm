package edu.rhit.groupalarm.groupalarm.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import edu.rhit.groupalarm.groupalarm.R;
import edu.rhit.groupalarm.groupalarm.User;

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.SearchFriendViewHolder> {
    private ArrayList<User> searchedUsers;
    private DatabaseReference mUserRef;
    private Query mSearchRef;
    private Context mContext;

    public SearchFriendAdapter(Context context) {
        searchedUsers = new ArrayList<>();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("users");
        mContext = context;
    }

    @NonNull
    @Override
    public SearchFriendAdapter.SearchFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_friend, parent, false);
        return new SearchFriendAdapter.SearchFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFriendAdapter.SearchFriendViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return searchedUsers.size();
    }

    public class SearchFriendViewHolder extends RecyclerView.ViewHolder {
        public SearchFriendViewHolder(View itemView) {
            super(itemView);
        }
    }
}
