package com.ging.chatapp.controller.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import com.ging.chatapp.controller.iterface.BaseView;

public class Controller<V extends BaseView> implements BaseView {
    
    protected V view;

    public Controller(@NonNull V view) {
        this.view = view;
    }

    @NonNull
    protected V getView() {
        return view;
    }

    @Nullable
    @Override
    public <T extends Controller> T addController(Class<T> clazz) {
        return view.addController(clazz);
    }

    @Nullable
    @Override
    public <T extends Controller> T setController(Class<T> clazz) {
        return view.setController(clazz);
    }

    @Nullable
    @Override
    public <T extends Controller> T getController(Class<T> clazz) {
        return view.getController(clazz);
    }

    @NonNull
    @Override
    public <T extends Controller> List<T> getControllers(Class<T> clazz) {
        return view.getControllers(clazz);
    }

    @NonNull
    @Override
    public List<Controller> getAllControllers() {
        return view.getAllControllers();
    }

    @Override
    public <T extends Controller> void removeController(Class<T> clazz) {
        view.removeController(clazz);
    }

    @Override
    public <T extends Controller> void removeControllers(Class<T> clazz) {
        view.removeControllers(clazz);
    }

    @Override
    public void removeAllControllers() {
        view.removeAllControllers();
    }

}
