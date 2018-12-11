package com.ging.chatapp.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import com.ging.chatapp.item.BaseItem;

public class ListUtils {

    public static <Item extends BaseItem> int findIndex(@Nullable List<Item> list, @Nullable Item item) {
        if(list == null || item == null) return -1;

        for(int i = list.size() - 1; i >= 0; i--) {
            if(list.get(i).getId() == item.getId()) return i;
        }

        return -1;
    }

    //
    public static boolean isValidateIndex(@NonNull List list, int index) {
        return index >= 0 && index < list.size();
    }

}
