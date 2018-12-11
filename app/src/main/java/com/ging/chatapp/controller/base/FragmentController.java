package com.ging.chatapp.controller.base;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ging.chatapp.controller.iterface.FragmentLifecycle;
import com.ging.chatapp.utils.ModelUtils;

public abstract class FragmentController<Model extends ViewModel> extends Controller<BaseFragment>
        implements FragmentLifecycle {

    protected final String TAG = getClass().getSimpleName();

    // Model Owner
    public static final String MODEL_OWNER = "model_owner";

    public static final int APP_MODEL = 1;
    public static final int ACTIVITY_MODEL = 2;
    public static final int PARENT_FRAGMENT_MODEL = 3;

    protected int modelOwner = ACTIVITY_MODEL;

    protected Model model;

    public FragmentController(BaseFragment view) {
        super(view);
    }

    @Override
    public void onAttach(Activity activity) {}

    @Override
    public void onAttach(Context context) {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        modelOwner = getModelOwner();
        model = onCreateModel(modelOwner);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {}

    @Override
    public void onStart() {}

    @Override
    public void onResume() {}

    @Override
    public void onPause() {}

    @Override
    public void onStop() {}

    @Override
    public void onDestroyView() {}

    @Override
    public void onDestroy() {}

    @Override
    public void onDetach() {}

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {}

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {}

    @Override
    public void onPrepareOptionsMenu(Menu menu) {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    // abstract
    @NonNull
    protected abstract Model onCreateModel(int modelOwner);

    //
    protected int getModelOwner() {
        if(getView().getArguments() != null) return getView().getArguments().getInt(MODEL_OWNER, modelOwner);
        return modelOwner;
    }

    // Model
    @Nullable
    protected <Model extends ViewModel> Model getAppModel(Class<Model> clazz) {
        if(getApplication() == null) return null;
        return ModelUtils.ofApp(getApplication()).get(clazz);
    }

    @Nullable
    protected <Model extends ViewModel> Model getActivityModel(Class<Model> clazz) {
        if(getActivity() == null) return null;
        return ModelUtils.of(getActivity()).get(clazz);
    }

    @Nullable
    protected <Model extends ViewModel> Model getParentFragmentModel(Class<Model> clazz) {
        if(getParentFragment() == null) return null;
        return ModelUtils.of(getParentFragment()).get(clazz);
    }

    @NonNull
    protected <Model extends ViewModel> Model getFragmentModel(Class<Model> clazz) {
        return ModelUtils.of(getFragment()).get(clazz);
    }

    @Nullable
    protected <Model extends ViewModel> Model getModel(int owner, Class<Model> clazz) {
        switch (owner) {
            case APP_MODEL:
                return getAppModel(clazz);

            case ACTIVITY_MODEL:
                return getActivityModel(clazz);

            case PARENT_FRAGMENT_MODEL:
                return getParentFragmentModel(clazz);

            default:
                return getFragmentModel(clazz);
        }
    }

    //
    protected <T> void observe(LiveData<T> liveData, Observer<T> observer) {
        liveData.observe(getLifecycleOwner(), observer);
    }

    @NonNull
    protected LifecycleOwner getLifecycleOwner() {
        return getFragment();
    }

    @Nullable
    protected Context getContext() {
        return getFragment().getContext();
    }

    @NonNull
    protected Resources getResources() {
        return getFragment().getResources();
    }

    @NonNull
    protected String getString(@StringRes int resId) {
        return getFragment().getString(resId);
    }

    @Nullable
    protected FragmentManager getFragmentManager() {
        return getFragment().getFragmentManager();
    }

    @Nullable
    FragmentManager getChildFragmentManager() {
        return getFragment().getChildFragmentManager();
    }

    @NonNull
    protected Fragment getFragment() {
        return getView();
    }

    @Nullable
    protected Fragment getParentFragment() {
        return getFragment().getParentFragment();
    }

    @Nullable
    protected FragmentActivity getActivity() {
        return getFragment().getActivity();
    }

    @Nullable
    protected Application getApplication() {
        if(getActivity() == null) return null;
        return getActivity().getApplication();
    }

    protected void setHasOptionsMenu(boolean hasMenu) {
        getFragment().setHasOptionsMenu(hasMenu);
    }
}
