package com.example.davidgeisinger.tennistracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class OverviewActivity extends AppCompatActivity {

    Button backHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Bundle mybundle = getIntent().getExtras();
        String message = mybundle.getString("string_to_overview");
        Log.d("LATERDATER", message);

        backHome = (Button) findViewById(R.id.backButton);
        backHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(OverviewActivity.this, HomeScreen.class);
                startActivity(intent);
            }
        });
    }
}
