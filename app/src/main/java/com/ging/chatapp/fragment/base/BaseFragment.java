package com.ging.chatapp.fragment.base;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ging.chatapp.config.Debug;
import com.ging.chatapp.model.AppModel;
import com.ging.chatapp.utils.ModelUtils;

public abstract class BaseFragment<Model extends ViewModel> extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    // Model Owner
    public static final String MODEL_OWNER = "model_owner";

    public static final int APP_MODEL = 1;
    public static final int ACTIVITY_MODEL = 2;
    public static final int PARENT_FRAGMENT_MODEL = 3;

    protected int modelOwner = ACTIVITY_MODEL;

    protected Model model;

    //
    protected boolean isDestroy = true;
    protected boolean isDestroyView = true;

    //
    protected Handler handler = new Handler();

    //
    public boolean isDestroy() {
        return isDestroy;
    }

    public boolean isDestroyView() {
        return isDestroyView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(Debug.TAG + TAG, "onAttach Context");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(Debug.TAG + TAG, "onAttach Activity");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Debug.TAG + TAG, "onCreate");
        isDestroy = false;

        modelOwner = getModelOwner();
        model = onCreateModel(modelOwner);

        observe(getAppModel(AppModel.class).getTheme(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if(integer != null) onThemeChange(integer);
            }
        });
    }

    protected void onThemeChange(int theme) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onUpdateTheme();
            }
        });
    }

    protected void onUpdateTheme() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(Debug.TAG + TAG, "onCreateView");
        return inflater.inflate(getFragmentLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Debug.TAG + TAG, "onViewCreated");
        isDestroyView = false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(Debug.TAG + TAG, "onActivityCreated");
        onUpdateTheme();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Debug.TAG + TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Debug.TAG + TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(Debug.TAG + TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(Debug.TAG + TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(Debug.TAG + TAG, "onDestroyView");
        isDestroyView = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Debug.TAG + TAG, "onDestroy");
        isDestroy = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(Debug.TAG + TAG, "onDetach");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(Debug.TAG + TAG, "onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(Debug.TAG + TAG, "onViewStateRestored");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(Debug.TAG + TAG, "onCreateOptionsMenu");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(Debug.TAG + TAG, "onPrepareOptionsMenu");
    }

    // abstract
    protected abstract int getFragmentLayoutId();

    protected abstract Model onCreateModel(int modelOwner);

    //
    protected int getModelOwner() {
        if(getArguments() != null) return getArguments().getInt(MODEL_OWNER, modelOwner);
        return modelOwner;
    }

    protected <T> void observe(LiveData<T> liveData, Observer<T> observer) {
        liveData.observe(getLifecycleOwner(), observer);
    }

    protected <T> void removeObserve(LiveData<T> liveData, Observer<T> observer) {
        liveData.removeObserver(observer);
    }

    protected <T> void removeObserves(LiveData<T> liveData) {
        liveData.removeObservers(getLifecycleOwner());
    }

    protected LifecycleOwner getLifecycleOwner() {
        return this;
    }

    // Model
    protected <Model extends ViewModel> Model getAppModel(Class<Model> clazz) {
        return ModelUtils.ofApp(getActivity().getApplication()).get(clazz);
    }

    protected <Model extends ViewModel> Model getActivityModel(Class<Model> clazz) {
        return ModelUtils.of(getActivity()).get(clazz);
    }

    protected <Model extends ViewModel> Model getParentFragmentModel(Class<Model> clazz) {
        return ModelUtils.of(getParentFragment()).get(clazz);
    }

    protected <Model extends ViewModel> Model getFragmentModel(Class<Model> clazz) {
        return ModelUtils.of(this).get(clazz);
    }

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
}
