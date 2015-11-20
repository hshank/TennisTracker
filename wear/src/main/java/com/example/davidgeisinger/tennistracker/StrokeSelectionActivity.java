package com.example.davidgeisinger.tennistracker;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StrokeSelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroke_selection);
    }

    public void startListener(View view) {
        Intent intent = new Intent(this, ListeningActivity.class);
        intent.putExtra("motion", (String) view.getTag());
        startActivity(intent);
    }
}
