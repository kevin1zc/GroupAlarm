package edu.rhit.groupalarm.groupalarm.Adapters;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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

import edu.rhit.groupalarm.groupalarm.Fragments.MainFragment;
import edu.rhit.groupalarm.groupalarm.R;
import edu.rhit.groupalarm.groupalarm.User;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private ArrayList<User> userList;
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");

    public SearchAdapter() {
        userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getmUsername());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setSearchUsername(String username) {
        userList.clear();
        Query query = userRef.orderByChild("mUsername").startAt(username).endAt(username + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    userList.add(user);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void queryNewFriend(String friendUsername) {
        Query query = userRef.orderByChild("mUsername").equalTo(friendUsername);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    User user = childSnapshot.getValue(User.class);
                    addNewFriend(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addNewFriend(User other) {
        HashMap<String, Boolean> otherFriendList = other.getmFriendList();
        otherFriendList.put(MainFragment.getCurrentUserInstance().getmUid(), false);
        userRef.child(other.getmUid()).child("mFriendList").setValue(otherFriendList);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private ImageView imageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.new_friend_textView);
            imageView = itemView.findViewById(R.id.imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    queryNewFriend(nameTextView.getText().toString());
                    Snackbar.make(itemView, "Request Sent", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

}
