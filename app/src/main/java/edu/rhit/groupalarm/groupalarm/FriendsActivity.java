package edu.rhit.groupalarm.groupalarm;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import edu.rhit.groupalarm.groupalarm.Fragments.FriendListFragment;
import edu.rhit.groupalarm.groupalarm.Fragments.FriendRequestFragment;
import edu.rhit.groupalarm.groupalarm.Fragments.SearchFragment;

public class FriendsActivity extends AppCompatActivity implements FriendListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_friends);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container_friends, new FriendListFragment());
        ft.commit();
    }

    @Override
    public void onFragmentInteraction() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_friends, FriendRequestFragment.newInstance());
        ft.addToBackStack("FriendList");
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_friend) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_friends, new SearchFragment());
            ft.addToBackStack("FriendList");
            ft.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
