package com.ging.chat.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ging.chat.fragment.HomeFragment;
import com.ging.chat.fragment.ProfileFragment;

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
