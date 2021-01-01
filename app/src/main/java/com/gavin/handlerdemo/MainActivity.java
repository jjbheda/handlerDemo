package com.gavin.handlerdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.os.Process;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    Handler handler = new Handler(Looper.myLooper());
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        Log.d(TAG, "1--》" + Thread.currentThread().getId());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tv.setText("oncreate方法，无耗时，改变Tv 文本");
            }
        }).start();
    }

    public void changeThreadBtnOnclick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "2---》" + Thread.currentThread().getId());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "3---》" + Thread.currentThread().getId());
                        tv.setText("切换线程，改变Tv 文本");
                    }
                });
            }
        }).start();
    }

    public void runOnUIBtnOnclick(View view) {
     runOnUiThread(new Runnable() {  // Activity 方法，只能在Activity中使用
         @Override
         public void run() {
             tv.setText("runOnUiThread 改变Tv 文本"); // 最方便
         }
     });
    }

    public void jumpToSec(View view) {
       Intent intent = new Intent(MainActivity.this, SecActivity.class );
       startActivity(intent);
    }

    public void viewpostTest(View view) {
        tv.post(new Runnable() {
            @Override
            public void run() {
                tv.setText("view.post方法，改变Tv 文本");
            }
        });
    }
}