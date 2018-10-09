package com.test.background;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int JOB_1 = 1;
    private static final int JOB_2 = 2;
    private static final int JOB_1_RESPONSE = 3;
    private static final int JOB_2_RESPONSE = 4;
    Messenger messenger = null;
    Button btnFirst, btnSecond;
    TextView txtMessage;
    boolean isBinder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: ");

        btnFirst = findViewById(R.id.btnFirst);
        btnSecond = findViewById(R.id.btnSecond);
        txtMessage = findViewById(R.id.txtMessage);

        btnFirst.setOnClickListener(this);
        btnSecond.setOnClickListener(this);

        Intent intent =  new Intent(this, MyService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
            isBinder = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messenger = null;
            isBinder = false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
        unbindService(serviceConnection);
        isBinder = false;

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");

    }

    @Override
    public void onClick(View view) {
        Message msg;
        if (view.getId() == R.id.btnFirst) {

            msg = Message.obtain(null, JOB_1);
            msg.replyTo = new Messenger(new ResponseHandler());

            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        } else if (view.getId() == R.id.btnSecond) {

            msg = Message.obtain(null, JOB_2);
            msg.replyTo = new Messenger(new ResponseHandler());

            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

    }


    class ResponseHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            String message;
            switch (msg.what) {
                case JOB_1_RESPONSE:
                    message = msg.getData().getString("response_message");
                    txtMessage.setText(message);
                    break;
                case JOB_2_RESPONSE:
                    message = msg.getData().getString("response_message");
                    txtMessage.setText(message);
                    break;
            }

            super.handleMessage(msg);
        }
    }

}
