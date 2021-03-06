package com.ging.chatapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ging.chatapp.config.Debug;
import com.ging.chatapp.config.Define;
import com.ging.chatapp.model.ChatModel;
import com.ging.chatapp.utils.ModelUtils;
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

    private static void emit(Context context, String event, String value) {
        Intent intent = new Intent(context, SocketService.class);
        intent.setAction("emit");

        Bundle data = new Bundle();
        data.putString("event", event);
        data.putString("value", value);
        intent.putExtras(data);

        context.startService(intent);
    }

    public static void emitAnswer(Context context, String answer) {
        emit(context, "answer", answer);
    }
}
