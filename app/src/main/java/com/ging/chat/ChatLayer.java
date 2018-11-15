package com.ging.chat;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ging.chat.config.Debug;
import com.ging.chat.model.ChatModel;
import com.ging.chat.service.SocketService;
import com.ging.chat.utils.ModelUtils;

/**
 * Creates the head layer view which is displayed directly on window manager.
 * It means that the view is above every application's view on your phone -
 * until another application does the same.
 */
public class ChatLayer extends FrameLayout {

    private WindowManager mWindowManager;

    private Observer<String> questionObserver;

    public ChatLayer(Context context) {
        super(context);

        addToWindowManager();
        inflateView();
    }

    private void addToWindowManager() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM;

        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(this, params);

        // Support dragging the image view
//        final ImageView imageView = (ImageView) mFrameLayout.findViewById(R.id.imageView);
////        imageView.setOnTouchListener(new OnTouchListener() {
////            private int initX, initY;
////            private int initTouchX, initTouchY;
////
////            @Override
////            public boolean onTouch(View v, MotionEvent event) {
////                int x = (int)event.getRawX();
////                int y = (int)event.getRawY();
////
////                switch (event.getAction()) {
////                    case MotionEvent.ACTION_DOWN:
////                        initX = params.x;
////                        initY = params.y;
////                        initTouchX = x;
////                        initTouchY = y;
////                        return true;
////
////                    case MotionEvent.ACTION_UP:
////                        return true;
////
////                    case MotionEvent.ACTION_MOVE:
////                        params.x = initX + (x - initTouchX);
////                        params.y = initY + (y - initTouchY);
////
////                        // Invalidate layout
////                        mWindowManager.updateViewLayout(mFrameLayout, params);
////                        return true;
////                }
////                return false;
////            }
////        });
    }

    private void inflateView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.chat_layer, this);

        ModelUtils.ofApp().get(ChatModel.class).getQuestion().observeForever(questionObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ((TextView) findViewById(R.id.question)).setText(s);
            }
        });

        RadioGroup answer = findViewById(R.id.answer);
        answer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SocketService.emitAnswer(getContext(),
                        ((RadioButton) findViewById(checkedId)).getText().toString());
            }
        });
    }

    /**
     * Removes the view from window manager.
     */
    public void destroy() {
        mWindowManager.removeView(this);
        ModelUtils.ofApp().get(ChatModel.class).getQuestion().removeObserver(questionObserver);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d(Debug.TAG, "dispatchKeyEvent");
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            // handle back press
//            // if (event.getAction() == KeyEvent.ACTION_DOWN)
//            return true;
//        }
        return super.dispatchKeyEvent(event);
//        return false;
    }

}
