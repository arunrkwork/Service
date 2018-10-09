package com.test.background;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    Button btnClick;
    Handler handler;
    Runnable runnable;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: ");
        handler = new Handler(Looper.getMainLooper());
        btnClick = findViewById(R.id.btnClick);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
                showDialog("Title");
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                showDialog(generateRandomWord(5));
                handler.postDelayed(this, 10000);
            }
        };

    }

    private void call() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse("https://developer.android.com"));
    }

    private void showDialog(final String title) {
        Log.i("MyThread", "showDialog: " + title);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();

                dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_view);

                TextView txtTitle;
                Button btnOk;

                txtTitle = dialog.findViewById(R.id.txtTitle);
                btnOk = dialog.findViewById(R.id.btnOk);

                txtTitle.setText(title);
                handler.post(runnable);
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Log.i("MyThread", "onDismiss: ");

                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handler.removeCallbacks(runnable);
                    }
                });

            }
        });
    }

    String generateRandomWord(int wordLength) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(wordLength);
        for(int i = 0; i < wordLength; i++) {
            char tmp = (char) ('a' + r.nextInt('z' - 'a'));
            sb.append(tmp); // Add it to the String
        }
        return sb.toString();
    }

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
        if (handler != null)
            handler.removeCallbacks(runnable);
    }
}
