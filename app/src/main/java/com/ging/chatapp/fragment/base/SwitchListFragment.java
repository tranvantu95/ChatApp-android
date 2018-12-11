package com.ging.chatapp.fragment.base;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ging.chatapp.adapter.base.SwitchListAdapter;
import com.ging.chatapp.config.Debug;
import com.ging.chatapp.model.base.SwitchListModel;

public abstract class SwitchListFragment<Item,
        Model extends SwitchListModel<Item>,
        LA extends SwitchListAdapter<Item, ?>>
        extends ListFragment<Item, Model, LA> {

    protected RecyclerView.LayoutManager linearLayoutManager;
    protected RecyclerView.LayoutManager gridLayoutManager;

    protected int dividerList, dividerGrid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        observeTypeView();
    }

    @NonNull
    @Override
    protected RecyclerView.LayoutManager onCreateLayoutManager() {
        linearLayoutManager = onCreateLinearLayoutManager();
        gridLayoutManager = onCreateGridLayoutManager();
        return linearLayoutManager;
    }

    @Dimension
    @Override
    protected int onCreateDivider() {
        dividerList = onCreateDividerList();
        dividerGrid = onCreateDividerGrid();
        return dividerList;
    }

    // abstract
    @NonNull
    protected abstract RecyclerView.LayoutManager onCreateLinearLayoutManager();

    @NonNull
    protected abstract RecyclerView.LayoutManager onCreateGridLayoutManager();

    @Dimension
    protected abstract int onCreateDividerList();

    @Dimension
    protected abstract int onCreateDividerGrid();

    // observe
    protected void observeTypeView() {
        observe(model.getTypeView(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if(integer != null) setTypeView(integer);
            }
        });
    }

    // set
    protected void setTypeView(int typeView) {
        if(listAdapter.getTypeView() == typeView) return;
        Log.d(Debug.TAG + TAG, "setTypeView " + typeView);

        listAdapter.setTypeView(typeView);
        layoutManager = switchLayoutManager(typeView);
        divider = switchDivider(typeView);

        if(listView != null) updateListView();
    }

    //
    private RecyclerView.LayoutManager switchLayoutManager(int typeView) {
        switch (typeView) {
            case SwitchListAdapter.LIST_VIEW:
                return linearLayoutManager;

            case SwitchListAdapter.GRID_VIEW:
                return gridLayoutManager;

            default:
                return linearLayoutManager;
        }
    }

    private int switchDivider(int typeView) {
        switch (typeView) {
            case SwitchListAdapter.LIST_VIEW:
                return dividerList;

            case SwitchListAdapter.GRID_VIEW:
                return dividerGrid;

            default:
                return dividerList;
        }
    }

}