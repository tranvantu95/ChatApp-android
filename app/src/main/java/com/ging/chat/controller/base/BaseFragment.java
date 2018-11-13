package com.ging.chat.controller.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ging.chat.config.Debug;
import com.ging.chat.controller.iterface.BaseView;

public abstract class BaseFragment extends Fragment implements BaseView {

    protected final String TAG = getClass().getSimpleName();

    // Controller
    protected List<Controller> controllers = new ArrayList<>();

    @Nullable
    @Override
    public <T extends Controller> T addController(Class<T> clazz) {
        if(!FragmentController.class.isAssignableFrom(clazz)) return null;

        T controller = null;

        try {
            controller = clazz.getConstructor(BaseFragment.class).newInstance(this);
            controllers.add(controller);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return controller;
    }

    @Nullable
    @Override
    public <T extends Controller> T setController(Class<T> clazz) {
        removeAllControllers();
        return addController(clazz);
    }

    @Nullable
    @Override
    public <T extends Controller> T getController(Class<T> clazz) {
        for(Controller controller : controllers)
            if(clazz.isInstance(controller)) return (T) controller;
        return null;
    }

    @NonNull
    @Override
    public <T extends Controller> List<T> getControllers(Class<T> clazz) {
        List<T> result = new ArrayList<>();
        for(Controller controller : controllers)
            if(clazz.isInstance(controller)) result.add((T) controller);
        return result;
    }

    @NonNull
    @Override
    public List<Controller> getAllControllers() {
        return controllers;
    }

    @Override
    public <T extends Controller> void removeController(Class<T> clazz) {
        for(Controller controller : controllers)
            if(clazz.isInstance(controller)) {
                controllers.remove(controller);
                return;
            }
    }

    @Override
    public <T extends Controller> void removeControllers(Class<T> clazz) {
        Iterator<Controller> iterator = controllers.iterator();
        while (iterator.hasNext()) {
            Controller controller = iterator.next();
            if(clazz.isInstance(controller)) iterator.remove();
        }
    }

    @Override
    public void removeAllControllers() {
        controllers.clear();
    }

    // LifeCycle
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(Debug.TAG + TAG, "onAttach Activity");
        addController();
        for(Controller controller : controllers)
            ((FragmentController) controller).onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(Debug.TAG + TAG, "onAttach Context");
        for(Controller controller : controllers)
            ((FragmentController) controller).onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Debug.TAG + TAG, "onCreate");
        for(Controller controller : controllers)
            ((FragmentController) controller).onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(Debug.TAG + TAG, "onCreateView");
        FrameLayout parent = new FrameLayout(container.getContext());
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parent.setLayoutParams(params);
        parent.addView(inflater.inflate(getFragmentLayoutId(), container, false));
        for(Controller controller : controllers) {
            View child = ((FragmentController) controller).onCreateView(inflater, container, savedInstanceState);
            if(child != null) parent.addView(child);
        }
        return parent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Debug.TAG + TAG, "onViewCreated");
        for(Controller controller : controllers)
            ((FragmentController) controller).onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(Debug.TAG + TAG, "onActivityCreated");
        for(Controller controller : controllers)
            ((FragmentController) controller).onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Debug.TAG + TAG, "onStart");
        for(Controller controller : controllers)
            ((FragmentController) controller).onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Debug.TAG + TAG, "onResume");
        for(Controller controller : controllers)
            ((FragmentController) controller).onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(Debug.TAG + TAG, "onPause");
        for(Controller controller : controllers)
            ((FragmentController) controller).onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(Debug.TAG + TAG, "onStop");
        for(Controller controller : controllers)
            ((FragmentController) controller).onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(Debug.TAG + TAG, "onDestroyView");
        for(Controller controller : controllers)
            ((FragmentController) controller).onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Debug.TAG + TAG, "onDestroy");
        for(Controller controller : controllers)
            ((FragmentController) controller).onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(Debug.TAG + TAG, "onDetach");
        for(Controller controller : controllers)
            ((FragmentController) controller).onDetach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(Debug.TAG + TAG, "onSaveInstanceState");
        for(Controller controller : controllers)
            ((FragmentController) controller).onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(Debug.TAG + TAG, "onViewStateRestored");
        for(Controller controller : controllers)
            ((FragmentController) controller).onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(Debug.TAG + TAG, "onCreateOptionsMenu");
        for(Controller controller : controllers)
            ((FragmentController) controller).onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(Debug.TAG + TAG, "onPrepareOptionsMenu");
        for(Controller controller : controllers)
            ((FragmentController) controller).onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(Debug.TAG + TAG, "onOptionsItemSelected");
        for(Controller controller : controllers)
            if(((FragmentController) controller).onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    // abstract
    protected abstract void addController();

    @LayoutRes
    protected abstract int getFragmentLayoutId();

}
