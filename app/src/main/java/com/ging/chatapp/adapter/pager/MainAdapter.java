package com.ging.chatapp.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ging.chatapp.fragment.HomeFragment;
import com.ging.chatapp.fragment.ProfileFragment;

public class MainAdapter extends FragmentPagerAdapter {

    private Fragment homeFragment, profileFragment;

    public MainAdapter(FragmentManager fm) {
        super(fm);

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return homeFragment;
            case 1:
                return profileFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
