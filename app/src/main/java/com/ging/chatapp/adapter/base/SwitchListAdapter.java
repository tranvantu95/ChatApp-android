package com.ging.chatapp.adapter.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.View;

public abstract class SwitchListAdapter<Item, VH extends SwitchListAdapter.ViewHolder<Item, ?>>
        extends ListAdapter<Item, VH> {

    public static final int LIST_VIEW = 1;
    public static final int GRID_VIEW = 2;

    private int typeView;

    public SwitchListAdapter(int mode, @NonNull OnItemClickListener<Item> onItemClickListener, @NonNull DiffUtil.ItemCallback<Item> diffCallback) {
        super(mode, onItemClickListener, diffCallback);
    }

    public int getTypeView() {
        return typeView;
    }

    public void setTypeView(int typeView) {
        this.typeView = typeView;
    }

    @LayoutRes
    @Override
    protected int getItemLayoutId(int viewType) {
        switch (getTypeView()) {
            case LIST_VIEW:
                return getItemListLayoutId(viewType);

            case GRID_VIEW:
                return getItemGridLayoutId(viewType);

            default:
                return getItemListLayoutId(viewType);
        }
    }

    @LayoutRes
    protected abstract int getItemListLayoutId(int viewType);

    @LayoutRes
    protected abstract int getItemGridLayoutId(int viewType);

    public static abstract class ViewHolder<Item, RA extends SwitchListAdapter<Item, ?>>
            extends ListAdapter.ViewHolder<Item, RA> {

        public ViewHolder(RA adapter, View itemView) {
            super(adapter, itemView);
        }
    }

}
