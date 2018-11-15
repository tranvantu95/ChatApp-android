package com.ging.chat.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ging.chat.R;
import com.ging.chat.activity.base.BaseActivity;
import com.ging.chat.chatheads.PermissionChecker;
import com.ging.chat.service.ChatService;

public class MainActivity extends BaseActivity {

    private PermissionChecker mPermissionChecker;

    private static boolean chatServiceCheckboxChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermissionChecker = new PermissionChecker(this);
        if(!mPermissionChecker.isRequiredPermissionGranted()){
            enableHeadServiceCheckbox(false);
            Intent intent = mPermissionChecker.createRequiredPermissionIntent();
            startActivityForResult(intent, PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE);
        } else {
            enableHeadServiceCheckbox(true);
        }

        ((CheckBox) findViewById(R.id.checkbox)).setChecked(chatServiceCheckboxChecked);

        ((CheckBox) findViewById(R.id.checkbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chatServiceCheckboxChecked = isChecked;
                if(isChecked) startChatService();
                else stopChatService();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE) {
            if (!mPermissionChecker.isRequiredPermissionGranted()) {
                Toast.makeText(getApplicationContext(), "Required permission is not granted. Please restart the app and grant required permission.", Toast.LENGTH_LONG).show();
            } else {
                enableHeadServiceCheckbox(true);
            }
        }
    }

    private void enableHeadServiceCheckbox(boolean enabled) {
        ((CheckBox) findViewById(R.id.checkbox)).setEnabled(enabled);
    }

    private void startChatService() {
        startService(new Intent(this, ChatService.class));
    }

    private void stopChatService() {
        stopService(new Intent(this, ChatService.class));
    }

}
