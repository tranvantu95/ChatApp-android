package com.ging.chat;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ging.chat.config.Debug;
import com.ging.chat.config.Define;
import com.ging.chat.model.ChatModel;
import com.ging.chat.service.ChatService;
import com.ging.chat.utils.AppUtils;
import com.ging.chat.utils.ModelUtils;

/**
 * Creates the head layer view which is displayed directly on window manager.
 * It means that the view is above every application's view on your phone -
 * until another application does the same.
 */
public class ChatLayer extends FrameLayout {

    private WindowManager mWindowManager;

    private WindowManager.LayoutParams params;

    private Observer<String> questionObserver, answerObserver;

    private Point point = new Point();

    private View trashLayer;
    private Rect trashRect = new Rect();

    private boolean opened;

    private HardwareKeyWatcher mHardwareKeyWatcher;

    public ChatLayer(Context context) {
        super(context);

        addToWindowManager();
        inflateView();

        openChatHub(true);

        mHardwareKeyWatcher = new HardwareKeyWatcher(context);
        mHardwareKeyWatcher.setOnHardwareKeysPressedListenerListener(new HardwareKeyWatcher.OnHardwareKeysPressedListener() {
            @Override
            public void onHomePressed() {
                Log.d(Debug.TAG, "onHomePressed");
                if(opened) toggleChatHub();
            }

            @Override
            public void onRecentAppsPressed() {
                Log.d(Debug.TAG, "onRecentAppsPressed");
                if(opened) toggleChatHub();
            }
        });
        mHardwareKeyWatcher.startWatch();
    }

    private void addToWindowManager() {
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        WindowManager.LayoutParams params2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        params2.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        trashLayer = LayoutInflater.from(getContext()).inflate(R.layout.layout_trash, null);

        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(trashLayer, params2);
        mWindowManager.addView(this, params);

        post(new Runnable() {
            @Override
            public void run() {
                trashRect = getRectOnScreen(trashLayer);
                trashLayer.setVisibility(GONE);

//                Log.d(Debug.TAG, "x: " + trashRect.centerX() + ", y: " + trashRect.centerY());
//                Log.d(Debug.TAG, "left: " + trashRect.left + ", right: " + trashRect.right);
//                Log.d(Debug.TAG, "top: " + trashRect.top + ", bot: " + trashRect.bottom);
//                Log.d(Debug.TAG, "width: " + trashRect.width() + ", height: " + trashRect.height());

            }
        });
    }

    private void inflateView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.layout_chat, this);

        findViewById(R.id.btn_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatHub(false);
            }
        });

        ModelUtils.ofApp().get(ChatModel.class).getQuestion().observeForever(questionObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ((TextView) findViewById(R.id.question)).setText(s);
            }
        });

        ModelUtils.ofApp().get(ChatModel.class).getAnswer().observeForever(answerObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(s == null) return;
                int id = Define.Answer.mapIds.get(s);
                ((RadioButton) findViewById(id)).setChecked(true);
            }
        });

        RadioGroup answer = findViewById(R.id.answer);
        answer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String answer = ((RadioButton) findViewById(checkedId)).getText().toString().toUpperCase();
                ChatService.emitAnswer(getContext().getApplicationContext(), answer);
            }
        });

        // Support dragging the image view
        ImageView iconChat = findViewById(R.id.icon_chat);
        iconChat.setOnTouchListener(new OnTouchListener() {
            private int initTouchX, initTouchY;
            private int initX, initY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initTouchX = x;
                        initTouchY = y;
                        initX = params.x;
                        initY = params.y;
                        trashLayer.setVisibility(VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                        int sx = params.x - initX;
                        int sy = params.y - initY;
                        float s = (float) Math.sqrt(sx*sx + sy*sy);
                        s = AppUtils.convertPixelsToDp(s, v.getContext());
                        if(s < 1) v.performClick();

                        trashLayer.setVisibility(GONE);
                        Rect rect = getRectOnScreen(ChatLayer.this);
                        if(intersect(trashRect, rect)) v.getContext().stopService(new Intent(v.getContext(), ChatService.class));

                        return true;

                    case MotionEvent.ACTION_MOVE:
                        int dx = x - initTouchX;
                        int dy = y - initTouchY;
                        params.x = initX + dx;
                        params.y = initY + dy;
                        updateThis();

                        post(new Runnable() {
                            @Override
                            public void run() {
                                Rect rect = getRectOnScreen(ChatLayer.this);
                                if(intersect(trashRect, rect)) trashLayer.setBackgroundColor(Color.RED);
                                else trashLayer.setBackgroundColor(Color.YELLOW);
                            }
                        });

                        return true;
                }
                return false;
            }
        });

        iconChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatHub(true);
            }
        });
    }

    private boolean intersect(Rect rect1, Rect rect2) {
        return rect1.left < rect2.right
                && rect1.right > rect2.left
                && rect1.top < rect2.bottom
                && rect1.bottom > rect2.top;
    }

    private Rect getRectOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Rect(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }

    private void updateThis() {
        mWindowManager.updateViewLayout(this, params);
    }

    private void toggleChatHub() {
        openChatHub(!opened);
    }

    private void openChatHub(boolean isOpen) {
        this.opened = isOpen;
        findViewById(R.id.icon_chat).setVisibility(!isOpen ? VISIBLE : GONE);
        findViewById(R.id.chat_hub).setVisibility(isOpen ? VISIBLE : GONE);
        params.width = isOpen ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = isOpen ? WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                : WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = isOpen ? Gravity.BOTTOM : 0;
        if(isOpen) {
            point.x = params.x;
            point.y = params.y;
            params.x = 0;
            params.y = 0;
        }
        else {
            params.x = point.x;
            params.y = point.y;
        }
        updateThis();
    }

    /**
     * Removes the view from window manager.
     */
    public void destroy() {
        mHardwareKeyWatcher.stopWatch();
        mWindowManager.removeView(this);
        mWindowManager.removeView(trashLayer);
        ModelUtils.ofApp().get(ChatModel.class).getQuestion().removeObserver(questionObserver);
        ModelUtils.ofApp().get(ChatModel.class).getAnswer().removeObserver(answerObserver);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d(Debug.TAG, "dispatchKeyEvent " + event.getKeyCode());
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if(opened) toggleChatHub();
            }
        }
        return super.dispatchKeyEvent(event);
    }

}
