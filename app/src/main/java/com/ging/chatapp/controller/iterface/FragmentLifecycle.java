package com.ging.chatapp.controller.iterface;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public interface FragmentLifecycle {

    public abstract void onAttach(Activity activity);

    public abstract void onAttach(Context context);

    public abstract void onCreate(@Nullable Bundle savedInstanceState);

    @Nullable
    public abstract View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    public abstract void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState);

    public abstract void onActivityCreated(@Nullable Bundle savedInstanceState);

    public abstract void onStart();

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onStop();

    public abstract void onDestroyView();

    public abstract void onDestroy();

    public abstract void onDetach();

    public abstract void onSaveInstanceState(@NonNull Bundle outState);

    public abstract void onViewStateRestored(@Nullable Bundle savedInstanceState);

    public abstract void onCreateOptionsMenu(Menu menu, MenuInflater inflater);

    public abstract void onPrepareOptionsMenu(Menu menu);

    public abstract boolean onOptionsItemSelected(MenuItem item);

}
