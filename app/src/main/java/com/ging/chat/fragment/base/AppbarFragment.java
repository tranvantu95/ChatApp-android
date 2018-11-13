package com.ging.chat.fragment.base;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.ging.chat.R;

public abstract class AppbarFragment<Model extends ViewModel> extends BaseFragment<Model> {

    protected AppBarLayout appbar;
    protected Toolbar toolbar;
    protected TabLayout tabs;
    protected ImageView appbarImage;
    protected CollapsingToolbarLayout collapsingToolbar;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    protected void initView(View view) {
        findView(view);
        setupActionBar();
    }

    protected void findView(View view) {
        appbar = view.findViewById(R.id.appbar);
        toolbar = view.findViewById(R.id.toolbar);
        tabs = view.findViewById(R.id.tabs);
        appbarImage = view.findViewById(R.id.app_bar_image);
    }

    // ActionBar
    protected void setupActionBar() {
        toolbar.setContentInsetStartWithNavigation(0);
        if(getActivity() != null && getActivity() instanceof AppCompatActivity)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    protected void showHomeButton() {
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if(actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                return;
            }
        }

        if(toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackClick();
                }
            });
        }

        else if(getActivity() != null && getActivity().getActionBar() != null)
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setTitle(int resStringId) {
        setTitle(getString(resStringId));
    }

    public void setTitle(CharSequence text) {
        if(collapsingToolbar != null && collapsingToolbar.isTitleEnabled()) {
            collapsingToolbar.setTitle(text);
            return;
        }

        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if(actionBar != null) {
                actionBar.setTitle(text);
                return;
            }
        }

        if(toolbar != null) toolbar.setTitle(text);

        else if(getActivity() != null && getActivity().getActionBar() != null)
            getActivity().getActionBar().setTitle(text);
    }

    public CharSequence getTitle() {
        if(collapsingToolbar != null && collapsingToolbar.isTitleEnabled())
            return collapsingToolbar.getTitle();

        if(getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if(actionBar != null) return actionBar.getTitle();
        }

        if(toolbar != null) return toolbar.getTitle();

        if(getActivity() != null && getActivity().getActionBar() != null)
            return getActivity().getActionBar().getTitle();

        return null;
    }

    // Collapsing
    public void setCollapsingTitleEnable(boolean enable) {
        collapsingToolbar.setTitleEnabled(enable);
    }

    // Tabs
    protected void setTitleTap(int index, int resStringId) {
        setTitleTap(index, getString(resStringId));
    }

    protected void setTitleTap(int index, String string) {
        TabLayout.Tab tab = tabs.getTabAt(index);
        if(tab != null) tab.setText(string);
    }

    //
    protected void onBackClick() {
        if(getActivity() != null) getActivity().onBackPressed();
    }
}
