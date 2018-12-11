package com.ging.chatapp.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ChatModel extends ViewModel {

    private MutableLiveData<String> question;

    public MutableLiveData<String> getQuestion() {
        if(question == null) question = new MutableLiveData<>();
        return question;
    }

    private MutableLiveData<String> answer;

    public MutableLiveData<String> getAnswer() {
        if(answer == null) answer = new MutableLiveData<>();
        return answer;
    }

}
