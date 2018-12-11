package com.ging.chatapp.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

public class MyCoordinatorLayoutBehavior extends CoordinatorLayout.Behavior<View> {

    public MyCoordinatorLayoutBehavior() {
    }

    public MyCoordinatorLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        int offset = -dependency.getTop() * child.getHeight() / dependency.getHeight();
//        android.util.Log.d("debug", "offset " + offset);
        child.setTranslationY(offset);
        return false;
    }
}
