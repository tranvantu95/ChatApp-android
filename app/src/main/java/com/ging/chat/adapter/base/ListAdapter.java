package com.ging.chat.adapter.base;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.ging.chat.item.BaseItem;

public abstract class ListAdapter<Item, VH extends ListAdapter.ViewHolder<Item, ?>>
        extends PagedListAdapter<Item, VH> {

    protected static <Item extends BaseItem> DiffUtil.ItemCallback<Item> createDiffCallback(Class<Item> clazz) {
        return new DiffUtil.ItemCallback<Item>() {
            @Override
            public boolean areItemsTheSame(Item oldItem, Item newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(Item oldItem, Item newItem) {
                return oldItem.equals(newItem);
            }
        };
    }

    protected static <Item extends BaseItem> DiffUtil.ItemCallback<Item> createDiffCallback2(Class<Item> clazz) {
        return new DiffUtil.ItemCallback<Item>() {
            @Override
            public boolean areItemsTheSame(Item oldItem, Item newItem) {
                return oldItem.getKey().equals(newItem.getKey());
            }

            @Override
            public boolean areContentsTheSame(Item oldItem, Item newItem) {
                return oldItem.equals(newItem);
            }
        };
    }

    public static final int RECYCLER_ADAPTER_MODE = 1;
    public static final int PAGED_LIST_ADAPTER_MODE = 2;

    private int mode;

    private List<Item> items = new ArrayList<>();

    private OnItemClickListener<Item> onItemClickListener;

    public ListAdapter(int mode, @NonNull OnItemClickListener<Item> onItemClickListener, DiffUtil.ItemCallback<Item> diffCallback) {
        super(diffCallback);
        this.mode = mode;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if(mode == PAGED_LIST_ADAPTER_MODE) return super.getItemCount();
        return items.size();
    }

    @Nullable
    @Override
    public Item getItem(int position) {
        if(mode == PAGED_LIST_ADAPTER_MODE) return super.getItem(position);
        return items.get(position);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    //
    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        if(this.mode == mode) return;
        this.mode = mode;
        notifyDataSetChanged();
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        if(mode == PAGED_LIST_ADAPTER_MODE && items instanceof PagedList)
            submitList((PagedList<Item>) items);
        else {
            this.items = items;
            notifyDataSetChanged();
        }
    }

    public OnItemClickListener<Item> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<Item> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    protected void onItemClick(RecyclerView.ViewHolder viewHolder, View view, @NonNull Item item, int position) {
        onItemClickListener.onItemClick(viewHolder, view, item, position);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(viewType), parent, false);
        return getViewHolder(view, viewType);
    }

    @LayoutRes
    protected abstract int getItemLayoutId(int viewType);

    @NonNull
    protected abstract VH getViewHolder(View view, int viewType);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Item item = getItem(position);
        holder.setItem(item, position);
    }

    public static abstract class ViewHolder<Item, RA extends ListAdapter<Item, ?>>
            extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected RA adapter;

        protected Item item;

        public ViewHolder(RA adapter, View itemView) {
            super(itemView);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
        }

        public void setItem(@Nullable Item item, int position) {
            this.item = item;
            if(item != null) updateItem(item, position);
            else clearItem(position);
        }

        protected abstract void updateItem(@NonNull Item item, int position);

        protected abstract void clearItem(int position);

        @Override
        public void onClick(View view) {
            onClick(view, getAdapterPosition());
        }

        protected void onClick(View view, int position) {
            if(item != null) adapter.onItemClick(this, view, item, position);
        }
    }

    public interface OnItemClickListener<Item> {
        void onItemClick(RecyclerView.ViewHolder viewHolder, View view, @NonNull Item item, int position);
    }
}
