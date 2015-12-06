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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private static final int CONNECTION_TIME_OUT_MS = 5000;
    private static final String START_STATS = "/watchToPhoneStats";
    String nodeId = null;

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
        retrieveDeviceNode();

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
        final GoogleApiClient client = getGoogleApiClient(this);
        if (nodeId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("Sending Message", "send");
                    client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(client, nodeId, START_STATS, null);
                    client.disconnect();
                }
            }).start();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void retrieveDeviceNode() {
        final GoogleApiClient client = getGoogleApiClient(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    nodeId = nodes.get(0).getId();
                    Log.d("PhoneActivity", nodeId);
                }
                client.disconnect();
            }
        }).start();
    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
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
            shotsMade.setText(shots_in + "/" + shots_total);
        }
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

