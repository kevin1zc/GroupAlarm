package edu.rhit.groupalarm.groupalarm;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import edu.rhit.groupalarm.groupalarm.Fragments.FriendListFragment;
import edu.rhit.groupalarm.groupalarm.Fragments.FriendRequestFragment;

public class FriendsActivity extends AppCompatActivity implements FriendListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_friends);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container_friends, new FriendListFragment());
        ft.commit();

        RecyclerView view=findViewById(R.id.searchResult_recyclerView);
    }

    @Override
    public void onFragmentInteraction() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_friends, FriendRequestFragment.newInstance());
        ft.addToBackStack("FriendList");
        ft.commit();
    }
}
