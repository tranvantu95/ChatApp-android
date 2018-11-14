package com.ging.chat.activity;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.ging.chat.R;
import com.ging.chat.activity.base.BaseActivity;
import com.ging.chat.model.ChatModel;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_on_top);

        observe(getAppModel(ChatModel.class).getQuestion(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ((TextView) findViewById(R.id.question)).setText(s);
            }
        });
    }
}
