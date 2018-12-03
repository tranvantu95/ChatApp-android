package com.ging.chat.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.ging.chat.R;
import com.ging.chat.activity.base.BaseActivity;
import com.ging.chat.adapter.pager.MainAdapter;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private MainAdapter adapter;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        adapter = new MainAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                        break;

                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        viewPager.setCurrentItem(0, true);
                        return true;

                    case R.id.nav_profile:
                        viewPager.setCurrentItem(1, true);
                        return true;
                }
                return false;
            }
        });
    }

}
