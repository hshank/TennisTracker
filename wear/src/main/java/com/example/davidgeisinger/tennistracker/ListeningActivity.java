package com.example.davidgeisinger.tennistracker;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.SpeechRecognizer;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.content.ServiceConnection;

public class ListeningActivity extends Activity {

    private Messenger mServiceMessenger;
    String motion;
    private int mBindFlag;
    int shots_in;
    int shots_wide;
    int shots_long;
    int shots_net;
    int shots_total;
    Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);
        motion = getIntent().getStringExtra("motion");
        TextView nav = (TextView) this.findViewById(R.id.nav);
        int sessionId = getResources().getIdentifier("title_" + motion, "string", getPackageName());
        nav.setText(getString(sessionId));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        service = new Intent(this, VoiceRecognitionService.class);
        this.startService(service);
        mBindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ? 0 : Context.BIND_ABOVE_CLIENT;

    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, VoiceRecognitionService.class), mServiceConnection, mBindFlag);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceMessenger != null) {
            Log.d("Listene", "Stop");
            stopService(service);
            unbindService(mServiceConnection);
            mServiceMessenger = null;
        }
    }

    public void finishSession(View view) {
//      send results to phone
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    class ResponseHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            int respCode = msg.what;
            Log.d("RESPONSE", "HELLO");
            switch (respCode) {
                case VoiceRecognitionService.MSG_IN: {
//                    result = msg.getData().getString("respData");
//                    txtResult.setText(result);
                }
            }
        }

    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);
            Message msg = new Message();
//            msg.replyTo = new Messenger(new ResponseHandler());
            msg.what = VoiceRecognitionService.MSG_RECOGNIZER_START_LISTENING;
            try {
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceMessenger = null;
        }
    };
}

