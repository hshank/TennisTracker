package com.example.davidgeisinger.tennistracker;

import android.content.Intent;

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
    public void onMessageReceived(MessageEvent messageEvent) {
        if( messageEvent.getPath().equalsIgnoreCase(START_STATS)) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);


            Intent serviceIntent = new Intent(this, HomeScreen.class);
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            serviceIntent.putExtra("phone_data", value);
            serviceIntent.setAction("Data");
            startActivity(serviceIntent);



        } else {
            super.onMessageReceived(messageEvent);
        }

    }
}

