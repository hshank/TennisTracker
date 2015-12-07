package com.example.davidgeisinger.tennistracker;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by davidgeisinger on 11/8/15.
 */
public class PhoneListenerService extends WearableListenerService {
    private static final String START_STATS = "/watchToPhoneStats";

    private static final String TAG = "MESSAGEBACKTOPHONE";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.d("START", "START");
        return START_STICKY;
    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("HELLO", messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(START_STATS)) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent serviceIntent = new Intent("broadcastStats");
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            serviceIntent.putExtra("phone_data", value);
            startActivity(serviceIntent);

        } else {
            super.onMessageReceived(messageEvent);
        }

    }
}

