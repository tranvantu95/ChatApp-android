package com.ging.chatapp.controller.iterface;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import com.ging.chatapp.controller.base.Controller;

public interface BaseView {

    @Nullable
    public abstract <T extends Controller> T addController(Class<T> clazz);

    @Nullable
    public abstract <T extends Controller> T setController(Class<T> clazz);

    @Nullable
    public abstract <T extends Controller> T getController(Class<T> clazz);

    @NonNull
    public abstract <T extends Controller> List<T> getControllers(Class<T> clazz);

    @NonNull
    public abstract List<Controller> getAllControllers();

    public abstract <T extends Controller> void removeController(Class<T> clazz);

    public abstract <T extends Controller> void removeControllers(Class<T> clazz);

    public abstract void removeAllControllers();

}
