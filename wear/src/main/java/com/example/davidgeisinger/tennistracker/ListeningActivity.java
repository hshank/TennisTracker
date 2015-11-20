package com.example.davidgeisinger.tennistracker;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.speech.RecognitionListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Set;

public class ListeningActivity extends Activity implements RecognitionListener {

    String motion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);
        motion = getIntent().getStringExtra("motion");
        setMotion();
        TextView finishSession = (TextView) this.findViewById(R.id.finish);
        finishSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSession();
            }
        });
    }

    private void setMotion() {
        TextView nav = (TextView) this.findViewById(R.id.nav);
        int sessionId = getResources().getIdentifier("title_" + motion, "string", getPackageName());
        nav.setText(getString(sessionId));
    }

    private void saveSession() {
//      send results to phone
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}

