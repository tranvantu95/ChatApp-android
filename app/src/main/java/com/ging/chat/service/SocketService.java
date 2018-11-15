package com.ging.chat.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ging.chat.config.Debug;
import com.ging.chat.config.Define;
import com.ging.chat.model.ChatModel;
import com.ging.chat.utils.ModelUtils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketService extends Service {

    private static final String TAG = "SocketService";

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Define.Socket.HOST);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Debug.TAG + TAG, "onCreate");

        mSocket.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(Debug.TAG + TAG, "connected");
            }
        });

        mSocket.on("question", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String question = (String) args[0];
                ModelUtils.ofApp().get(ChatModel.class).getQuestion().postValue(question);
            }
        });

        mSocket.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Debug.TAG + TAG, "onDestroy");

        mSocket.disconnect();
        mSocket.off("connect");
        mSocket.off("question");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(("emit").equals(intent.getAction())) {
            Bundle data = intent.getExtras();
            if(data != null) {
                String event = data.getString("event", "");
                String value = data.getString("value", "");
                emit(event, value);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void emit(String event, String value) {
        if(mSocket == null || !mSocket.connected()) return;
        mSocket.emit(event, value);
    }

    public static void emitAnswer(Context context, String answer) {
        Intent intent = new Intent(context, SocketService.class);
        intent.setAction("emit");
        Bundle data = new Bundle();
        data.putString("event", "answer");
        data.putString("value", answer);
        intent.putExtras(data);

        context.startService(intent);
    }
}
