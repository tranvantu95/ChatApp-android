package com.ging.chatapp.activity.base;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.ging.chatapp.R;

public class AppbarActivity extends BaseActivity {

    protected AppBarLayout appbar;
    protected Toolbar toolbar;
    protected TabLayout tabs;
    protected ImageView appbarImage;
    protected CollapsingToolbarLayout collapsingToolbar;

    protected void init() {
        findView();
        setupActionBar();
    }

    protected void findView() {
        appbar = findViewById(R.id.appbar);
        toolbar = findViewById(R.id.toolbar);
        tabs = findViewById(R.id.tabs);
        appbarImage = findViewById(R.id.app_bar_image);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
    }

    // ActionBar
    protected void setupActionBar() {
        if(toolbar == null) return;
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
    }

    protected void showHomeButton() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        else if(toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackClick();
                }
            });
        }

        else if(getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setTitle(int resStringId) {
        setTitle(getString(resStringId));
    }

    public void setTitle(CharSequence text) {
        if(collapsingToolbar != null && collapsingToolbar.isTitleEnabled())
            collapsingToolbar.setTitle(text);

        else if(getSupportActionBar() != null) getSupportActionBar().setTitle(text);

        else if(toolbar != null) toolbar.setTitle(text);

        else super.setTitle(text);
    }

    public CharSequence getTitle2() {
        if(collapsingToolbar != null && collapsingToolbar.isTitleEnabled())
            return collapsingToolbar.getTitle();

        if(getSupportActionBar() != null)
            return getSupportActionBar().getTitle();

        if(toolbar != null) return toolbar.getTitle();

        return super.getTitle();
    }

    // Collapsing
    protected void setCollapsingTitleEnable(boolean enable) {
        collapsingToolbar.setTitleEnabled(enable);
    }

    // Tabs
    protected void setTitleTap(int index, int resStringId) {
        setTitleTap(index, getString(resStringId));
    }

    protected void setTitleTap(int index, CharSequence text) {
        TabLayout.Tab tab = tabs.getTabAt(index);
        if(tab != null) tab.setText(text);
    }

    // Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackClick();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onBackClick() {
        onBackPressed();
    }
}
