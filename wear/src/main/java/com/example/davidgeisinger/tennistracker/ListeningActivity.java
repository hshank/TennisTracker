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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.content.ServiceConnection;

public class ListeningActivity extends Activity {

    private Messenger mServiceMessenger;
    String motion;
    private int mBindFlag;
    protected final Messenger mResponseMessenger = new Messenger(new ResponseHandler());
    protected int shots_in;
    protected int shots_wide;
    protected int shots_long;
    protected int shots_net;
    protected int shots_total;
    private TextView shotsMade;
    Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);
        motion = getIntent().getStringExtra("motion");
        TextView nav = (TextView) this.findViewById(R.id.nav);
        int sessionId = getResources().getIdentifier("title_" + motion, "string", getPackageName());
        nav.setText(getString(sessionId));
        shotsMade = (TextView) this.findViewById(R.id.shotsMade);
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
            Log.d("bLSH", "HELLO");
            switch (respCode) {
                case VoiceRecognitionService.MSG_IN:
                    shots_in += 1;
                    break;
                case VoiceRecognitionService.MSG_LONG:
                    shots_long += 1;
                    break;
                case VoiceRecognitionService.MSG_WIDE:
                    shots_wide += 1;
                    break;
                case VoiceRecognitionService.MSG_NET:
                    shots_net += 1;
                    break;
            }
            shots_total += 1;
            updateView();
        }
    }

    protected void updateView() {
        shotsMade.setText(shots_in + "/" + shots_total);
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);
            Message msg = new Message();
//            msg.replyTo = new Messenger(new ResponseHandler());
            msg.replyTo = mResponseMessenger;
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

