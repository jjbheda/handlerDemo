package com.gavin.handlerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SecActivity extends AppCompatActivity {
    private static String TAG = "SecActivity";
    TextView tv;
    Handler threadHandler;

    Handler mainHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.obj != null) {
                Log.e(TAG,  "主线程收到了呼叫, \"" + msg.obj + "\"" );
            } else {
                Log.e(TAG,  "主线程收到了呼叫");
            }
            Message message = new Message();
            message.obj = "这是来自主线程的呼叫！！！";
            if (threadHandler != null) {
                threadHandler.sendMessageDelayed(message, 1000);
            }
        }
      };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);
        tv = findViewById(R.id.tv);
        HandlerThread thread = new HandlerThread("HandlerThread"); // 使用HandlerThread主要是方便获取Looper
        thread.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                threadHandler = new Handler(thread.getLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        if (msg.obj != null) {
                            Log.e(TAG,  "子线程收到了呼叫, \"" + msg.obj + "\"" );
                        } else {
                            Log.e(TAG,  "子线程收到了呼叫");
                        }
                        Message message = new Message();
                        message.obj = "这是来自子线程的呼叫！！！";
                        mainHandler.sendMessageDelayed(message,1000);

                    }
                };
            }
        }).start();
    }

    public void testMsgTransform(View view) {
        mainHandler.sendEmptyMessageDelayed(0, 100); // 发送一个空消息，被自身的handleMessage 捕获。谁发送 谁接收。
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //如果这里不移除 callback，这个callback会一直存在，容易引发内存泄漏
        mainHandler.removeCallbacksAndMessages(null);
        threadHandler.removeCallbacksAndMessages(null);

    }
}