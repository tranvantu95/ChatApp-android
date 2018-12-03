package com.ging.chat.fragment;

import android.annotation.TargetApi;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ging.chat.R;
import com.ging.chat.chatheads.PermissionChecker;
import com.ging.chat.fragment.base.BaseFragment;
import com.ging.chat.service.ChatService;

public class HomeFragment extends BaseFragment {

    private PermissionChecker mPermissionChecker;

    private static boolean chatServiceCheckboxChecked;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPermissionChecker = new PermissionChecker(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((CheckBox) view.findViewById(R.id.checkbox)).setChecked(chatServiceCheckboxChecked);

        ((CheckBox) view.findViewById(R.id.checkbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chatServiceCheckboxChecked = isChecked;
                if(isChecked) startChatService();
                else stopChatService();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(!mPermissionChecker.isRequiredPermissionGranted()){
            enableHeadServiceCheckbox(false);
            Intent intent = mPermissionChecker.createRequiredPermissionIntent();
            startActivityForResult(intent, PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE);
        } else {
            enableHeadServiceCheckbox(true);
        }
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected ViewModel onCreateModel(int modelOwner) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE) {
            if (!mPermissionChecker.isRequiredPermissionGranted()) {
                Toast.makeText(getContext().getApplicationContext(), "Required permission is not granted. Please restart the app and grant required permission.", Toast.LENGTH_LONG).show();
            } else {
                enableHeadServiceCheckbox(true);
            }
        }
    }

    private void enableHeadServiceCheckbox(boolean enabled) {
        ((CheckBox) getView().findViewById(R.id.checkbox)).setEnabled(enabled);
    }

    private void startChatService() {
        getActivity().startService(new Intent(getContext(), ChatService.class));
    }

    private void stopChatService() {
        getActivity().stopService(new Intent(getContext(), ChatService.class));
    }

}
