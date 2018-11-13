package com.ging.chat.model.base;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import java.util.List;

public class ListModel<Item> extends ViewModel {

    protected MutableLiveData<DataSource.Factory<?, Item>> dataSourceFactory;

    protected LiveData<PagedList<Item>> pagedList;

    protected MutableLiveData<List<Item>> items;

    protected MutableLiveData<Boolean> refresh;

    @NonNull
    public MutableLiveData<DataSource.Factory<?, Item>> getDataSourceFactory() {
        if(dataSourceFactory == null) dataSourceFactory = new MutableLiveData<>();
        return dataSourceFactory;
    }

    @NonNull
    public LiveData<PagedList<Item>> getPagedList(@NonNull final PagedList.Config config) {
        if(pagedList == null)
            pagedList = Transformations.switchMap(getDataSourceFactory(),
                new Function<DataSource.Factory<?, Item>, LiveData<PagedList<Item>>>() {
                    @Override
                    public LiveData<PagedList<Item>> apply(DataSource.Factory<?, Item> input) {
                        if(input != null) return new LivePagedListBuilder<>(input, config).build();
                        return null;
                    }
                });

        return pagedList;
    }

    @NonNull
    public MutableLiveData<List<Item>> getItems() {
        if(items == null) items = new MutableLiveData<>();
        return items;
    }

    @NonNull
    public MutableLiveData<Boolean> getRefresh() {
        if(refresh == null) refresh = new MutableLiveData<>();
        return refresh;
    }
}
