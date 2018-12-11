package com.ging.chatapp.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

public class MyBotTabLayoutBehavior extends CoordinatorLayout.Behavior<View> {

    private ValueAnimator animator;

    private View target;

    private float offset;

    private boolean anim;

    private static final int TIME_DELAY_UPDATE = 300;

    private boolean isUpdate = true;

    private Handler handler = new Handler();

    private Runnable callbackUpdate = new Runnable() {
        @Override
        public void run() {
            isUpdate = true;
        }
    };

    public void runtimeCallbackUpdate() {
        isUpdate = false;
//        handler.post(callbackUpdate);
        handler.removeCallbacks(callbackUpdate);
        handler.postDelayed(callbackUpdate, TIME_DELAY_UPDATE);
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public MyBotTabLayoutBehavior(View target, boolean anim) {
        this.target = target;
        this.anim = anim;
    }

    public MyBotTabLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if(!isUpdate || dependency.getHeight() == 0) return false;
        if(target == null) target = child;

        int offset = -dependency.getTop() * target.getHeight() / dependency.getHeight();
//        android.util.Log.d("debug", "offset " + offset);

        if(offset != this.offset) {
            this.offset = offset;
            updateOffset(anim);
        }

        return false;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public void updateOffset(boolean anim) {
        if(animator != null) animator.cancel();
        if(target.getTranslationY() == offset) return;

        if(!anim) {
            target.setTranslationY(offset);
            return;
        }

        animator = ValueAnimator.ofFloat(target.getTranslationY(), offset);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                target.setTranslationY((float) animation.getAnimatedValue());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animator = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.start();
    }

    public void cancelUpdateOffset() {
        if(animator != null) animator.cancel();
    }
}
