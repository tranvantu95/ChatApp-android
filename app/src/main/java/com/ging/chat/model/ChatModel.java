package com.ging.chat.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ChatModel extends ViewModel {

    private MutableLiveData<String> question;

    public MutableLiveData<String> getQuestion() {
        if(question == null) question = new MutableLiveData<>();
        return question;
    }
}
