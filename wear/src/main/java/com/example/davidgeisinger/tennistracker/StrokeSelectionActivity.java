package com.example.davidgeisinger.tennistracker;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

public class StrokeSelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_selection);
    }

    public void startListener(View view) {
        Intent intent = new Intent(this, ListeningActivity.class);
        startActivity(intent);
    }
}
