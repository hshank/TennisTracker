package com.example.davidgeisinger.tennistracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowSession extends AppCompatActivity {

    Button back_button;
    TextView shotsInText;
    TextView shotsLongText;
    TextView shotsWideText;
    TextView shotsNetText;
    TextView durationText;

    double practiceTime;
    int shotsMade;
    int shotsLong;
    int shotsWide;
    int shotsNet;
    int shotsTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_session);
        Bundle mybundle = getIntent().getExtras();
        String message = mybundle.getString("string_passed");
        Log.d("IN SHOW SESSION", message);
        back_button = (Button) findViewById(R.id.backtohome);
        shotsInText = (TextView) findViewById(R.id.success);
        shotsLongText = (TextView) findViewById(R.id.longg);
        shotsWideText = (TextView) findViewById(R.id.wide);
        shotsNetText = (TextView) findViewById(R.id.net);
        durationText = (TextView) findViewById(R.id.time);



        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShowSession.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        int [] arr = getShots(message);
        shotsMade = arr[0];
        shotsLong = arr[1];
        shotsWide = arr[2];
        shotsNet = arr[3];
        shotsTotal = shotsMade + shotsLong + shotsWide + shotsNet;

        practiceTime = getTime(message);

        makeChart();
        changeTexts();


    }

    //parse passed string to get array of shots made and missed
    public int[] getShots(String message) {

        return null;
    }

    //parse passed string to get duration
    public double getTime(String message) {

        return 0.0;
    }

    public void changeTexts() {
        shotsInText.setText("Successful: " + Integer.toString(shotsMade) + "/" + Integer.toString(shotsTotal) + " shots.");
        shotsLongText.setText("Long: " + Integer.toString(shotsLong) + "/" + Integer.toString(shotsTotal) + " shots.");
        shotsWideText.setText("Wide: " + Integer.toString(shotsWide) + "/" + Integer.toString(shotsTotal) + " shots.");
        shotsNetText.setText("Net: " + Integer.toString(shotsNet) + "/" + Integer.toString(shotsTotal) + " shots.");
        durationText.setText("HI");
    }

    //make the chart based on the shots statistics
    public void makeChart() {


    }
}
