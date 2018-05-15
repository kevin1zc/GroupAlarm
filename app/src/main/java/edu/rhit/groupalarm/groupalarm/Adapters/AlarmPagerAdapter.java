package edu.rhit.groupalarm.groupalarm.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import edu.rhit.groupalarm.groupalarm.Fragments.AlarmFragment;
import edu.rhit.groupalarm.groupalarm.User;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class AlarmPagerAdapter extends FragmentPagerAdapter {
    private User mUser;
    private Context mContext;
    private ViewPager mViewPager;

    public AlarmPagerAdapter(FragmentManager fm, User user, Context context,ViewPager viewPager) {
        super(fm);
        mUser = user;
        mContext = context;
        mViewPager=viewPager;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a AlarmFragment (defined as a static inner class below).

        return AlarmFragment.newInstance(position + 1, mUser, mContext,mViewPager);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}
