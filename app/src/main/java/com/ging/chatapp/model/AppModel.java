package com.ging.chatapp.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class AppModel extends ViewModel {

    private MutableLiveData<Integer> theme;

    public MutableLiveData<Integer> getTheme() {
        if(theme == null) theme = new MutableLiveData<>();
        return theme;
    }

    private MutableLiveData<Integer> typeView;

    public MutableLiveData<Integer> getTypeView() {
        if(typeView == null) typeView = new MutableLiveData<>();
        return typeView;
    }

    private MutableLiveData<User> user;

    public MutableLiveData<User> getUser() {
        if(user == null) user = new MutableLiveData<>();
        return user;
    }

}
