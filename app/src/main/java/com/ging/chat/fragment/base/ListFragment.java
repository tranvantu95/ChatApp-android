package com.ging.chat.fragment.base;

import android.arch.lifecycle.Observer;
import android.arch.paging.DataSource;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import com.ging.chat.R;
import com.ging.chat.adapter.base.ListAdapter;
import com.ging.chat.config.Debug;
import com.ging.chat.model.base.ListModel;

public abstract class ListFragment<Item,
        Model extends ListModel<Item>,
        LA extends ListAdapter<Item, ?>>
        extends BaseFragment<Model> {

    protected RecyclerView listView;

    protected RecyclerView.LayoutManager layoutManager;

    protected LA listAdapter;

    protected int divider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutManager = onCreateLayoutManager();
        listAdapter = onCreateListAdapter();
        divider = onCreateDivider();

        observeItems();

        observeRefresh();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.list_view);
        updateListView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        listView.setLayoutManager(null);
        listView.setAdapter(null);
        listView = null;
    }

    // abstract
    protected abstract PagedList.Config getPagedListConfig();

    @NonNull
    protected abstract RecyclerView.LayoutManager onCreateLayoutManager();

    @NonNull
    protected abstract LA onCreateListAdapter();

    @Dimension
    protected abstract int onCreateDivider();

    // observe
    protected void observeItems() {
        if(listAdapter.getMode() == ListAdapter.PAGED_LIST_ADAPTER_MODE)
            observe(model.getPagedList(getPagedListConfig()), new Observer<PagedList<Item>>() {
                @Override
                public void onChanged(@Nullable PagedList<Item> items) {
                    if (items != null) updateListAdapter(items);
                }
            });
        else
            observe(model.getItems(), new Observer<List<Item>>() {
                @Override
                public void onChanged(@Nullable List<Item> items) {
                    if (items != null) updateListAdapter(items);
                }
            });
    }

    protected void observeRefresh() {
        observe(model.getRefresh(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean != null && aBoolean) {
                    model.getRefresh().setValue(null);
                    refresh();
                }
            }
        });
    }

    // set
    protected void setDataSourceFactory(@Nullable DataSource.Factory<?, Item> factory) {
        model.getDataSourceFactory().setValue(factory);
    }

    // update
    protected void updateListAdapter(@NonNull List<Item> items) {
        Log.d(Debug.TAG + TAG, "updateListAdapter");
        listAdapter.setItems(items);
    }

    protected void updateListView() {
        Log.d(Debug.TAG + TAG, "updateListView");
        listView.setPadding(divider, divider, divider, divider);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(listAdapter);
    }

    protected void refresh() {
        listAdapter.notifyDataSetChanged();
    }

}
