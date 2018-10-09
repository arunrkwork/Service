package com.test.background;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by Arunraj on 10/9/2018
 */
public class MyService extends Service {

    private static final int JOB_1 = 1;
    private static final int JOB_2 = 2;
    private static final int JOB_1_RESPONSE = 3;
    private static final int JOB_2_RESPONSE = 4;
    Messenger messenger = new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    class IncomingHandler extends Handler {

        Message MSG;
        String message;
        Bundle bundle = new Bundle();

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case JOB_1:
                    message = "This is first message from service";
                    MSG = Message.obtain(null, JOB_1_RESPONSE);
                    bundle.putString("response_message", message);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                case JOB_2:
                    message = "This is second message from service";
                    MSG = Message.obtain(null, JOB_2_RESPONSE);
                    bundle.putString("response_message", message);
                    MSG.setData(bundle);

                    try {
                        msg.replyTo.send(MSG);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            super.handleMessage(msg);
        }
    }


}
