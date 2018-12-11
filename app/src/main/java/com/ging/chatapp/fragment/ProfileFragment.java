package com.ging.chatapp.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.ging.chatapp.R;
import com.ging.chatapp.dialog.LoginDialog;
import com.ging.chatapp.fragment.base.BaseFragment;
import com.ging.chatapp.model.AppModel;
import com.ging.chatapp.model.User;
import com.ging.chatapp.utils.ModelUtils;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvDisplayName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ModelUtils.ofApp().get(AppModel.class).getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if(user != null) loginView(user);
                else logoutView();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDisplayName = view.findViewById(R.id.tv_display_name);

        view.findViewById(R.id.btn_login).setOnClickListener(this);
        view.findViewById(R.id.btn_logout).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                showLoginDialog();
                break;

            case R.id.btn_logout:
                logout();
                break;
        }
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected ViewModel onCreateModel(int modelOwner) {
        return null;
    }

    private void showLoginDialog() {
        new LoginDialog().show(getFragmentManager(), "LoginDialog");
    }

    private void loginView(@NonNull User user) {
        tvDisplayName.setText(user.getDisplayName());
        getView().findViewById(R.id.btn_login).setVisibility(View.GONE);
        getView().findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
    }

    private void logoutView() {
        tvDisplayName.setText("");
        getView().findViewById(R.id.btn_login).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.btn_logout).setVisibility(View.GONE);
    }

    private void logout() {
        ModelUtils.ofApp().get(AppModel.class).getUser().setValue(null);
        logoutFacebook();
    }

    private void logoutFacebook() {
        LoginManager.getInstance().logOut();
    }

}
