package com.example.davidgeisinger.tennistracker;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class InstructionActivity extends Activity {

    String motion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        motion = getIntent().getStringExtra("motion");
        TextView nav = (TextView) this.findViewById(R.id.nav);
        int sessionId = getResources().getIdentifier("title_" + motion, "string", getPackageName());
        nav.setText(getString(sessionId));
    }

    public void beginSession(View view) {
//      send results to phone
        Intent intent = new Intent(this, ListeningActivity.class);
        intent.putExtra("motion", motion);
        startActivity(intent);
    }

}
