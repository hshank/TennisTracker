package com.example.davidgeisinger.tennistracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity {

    Button backHome;
    Context ctx = this;
    ArrayList<StatsPackage> items = new ArrayList<StatsPackage>();
    String avgShots;
    String mostShots;
    String leastShots;
    String mostShotsTotal;
    String leastShotsTotal;
    String mostShotsDate;
    String leastShotsDate;
    String color;

    String avgPracticeTime;
    String mostPracticeTime;
    String leastPracticeTime;



    TextView avgShotsText;
    TextView mostShotsText;
    TextView leastShotsText;
    TextView avgPracticeText;
    TextView mostPracticeText;
    TextView leastPracticeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_overview);
        Bundle mybundle = getIntent().getExtras();
        final String stroke = mybundle.getString("string_to_overview");
        Button button = (Button) findViewById(R.id.backButton);
        if (stroke.equals("f")){
            setTitle("Forehand Overview");
            color = "#218A6A";
            button.setBackgroundColor(Color.parseColor(color));
        }
        else if (stroke.equals("b")){
            setTitle("Backhand Overview");
            color = "#34A17B";
            button.setBackgroundColor(Color.parseColor(color));
        }
        else if (stroke.equals("s")){
            color = "#78AF62";
            setTitle("Serve Overview");
            button.setBackgroundColor(Color.parseColor(color));
        }
        else {
            setTitle("Volley Overview");
            color = "#B4B64D";
            button.setBackgroundColor(Color.parseColor(color));
        }

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(color));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        avgShotsText = (TextView) findViewById(R.id.avgMade);
        mostShotsText = (TextView) findViewById(R.id.mostMade);
        leastShotsText = (TextView) findViewById(R.id.leastMade);
        avgPracticeText = (TextView) findViewById(R.id.avgPractice);
        mostPracticeText = (TextView) findViewById(R.id.longestPractice);
        leastPracticeText = (TextView) findViewById(R.id.shortestPractice);

        MyDBHandler dbHandler = new MyDBHandler(ctx);

        items = dbHandler.findMany(stroke);

        calculateHandler();
        populateTextFields();


        backHome = (Button) findViewById(R.id.backButton);
        backHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(OverviewActivity.this, HomeScreen.class);
                //intent.setAction("passing_stroke");
                intent.putExtra("stroke", stroke);
                startActivity(intent);
            }
        });

        GraphView graph = (GraphView) findViewById(R.id.linegraph);
        ArrayList<DataPoint> dps = new ArrayList<DataPoint>();
        String made_stat;
        for (int i = 0; i < items.size(); i++) {
            made_stat = items.get(i).stats.split("\\$")[0];
            dps.add(new DataPoint(i, Integer.parseInt(made_stat)));
        }
        DataPoint[] dpArr = new DataPoint[items.size()];
        for (int i = 0; i < items.size(); i++) {
            dpArr[i] = dps.get(i);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dpArr);
        graph.addSeries(series);

        graph.setTitle(items.get(0).date.substring(0,12) + " - " + items.get(items.size() - 1).date.substring(0,12));
        graph.setTitleTextSize(100);
        series.setColor(Color.parseColor(color));
        Viewport viewPort = graph.getViewport();
        viewPort.setYAxisBoundsManual(true);
        viewPort.setMinY(0);
        viewPort.setMaxY(Integer.parseInt(mostShots) + 10);
    }



    public void calculateHandler() {
        calculate_avgShots();
        calculate_mostShots();
        calculate_leastShots();
        calculate_avgTime();
        calculate_mostTime();
        calculate_leastTime();

    }

    public void populateTextFields() {
        avgShotsText.setText(Html.fromHtml("<b>  Average Shots Made: </b> " + avgShots));
        mostShotsText.setText(Html.fromHtml("<b>  Most Shots Made: </b> " + mostShots + "/" + mostShotsTotal + "  (" + mostShotsDate + ")"));
        leastShotsText.setText(Html.fromHtml("<b>  Least Shots Made: </b> " + leastShots + "/" + leastShotsTotal + "  (" + leastShotsDate + ")\n"));
        avgPracticeText.setText(Html.fromHtml("<b>  Average Practice Length: </b> " + avgPracticeTime + " minutes"));
        mostPracticeText.setText(Html.fromHtml("<b>  Longest Practice: </b> " + mostPracticeTime + " minutes"));
        leastPracticeText.setText(Html.fromHtml("<b>  Shortest Practice: </b> " + leastPracticeTime + " minutes"));

    }

    //This will calculate my average shots made
    public void calculate_avgShots() {
        int sumShots = 0;
        for (int i = 0; i < items.size(); i++) {
            String stats = items.get(i).stats;
            String [] made = stats.split("\\$");
            sumShots += Integer.parseInt(made[0]);
        }
        int avgShotsInt = sumShots / items.size();
        avgShots = Integer.toString(avgShotsInt);
    }

    public void calculate_mostShots() {
        int bestShots = 0;
        for (int i = 0; i < items.size(); i++) {
            String stats = items.get(i).stats;
            String [] made = stats.split("\\$");
            if (Integer.parseInt(made[0]) > bestShots) {
                bestShots = Integer.parseInt(made[0]);
                mostShotsTotal = Integer.toString(Integer.parseInt(made[0]) + Integer.parseInt(made[1]) + Integer.parseInt(made[2]) + Integer.parseInt(made[3]));
                mostShotsDate = items.get(i).date;
                mostShotsDate = mostShotsDate.split(" ")[0] + " " + mostShotsDate.split(" ")[1].substring(0,2);
            }
        }
        mostShots = Integer.toString(bestShots);
    }

    public void calculate_leastShots() {
        int worstShots = 6969;
        for (int i = 0; i < items.size(); i++) {
            String stats = items.get(i).stats;
            String [] made = stats.split("\\$");
            if (Integer.parseInt(made[0]) < worstShots) {
                worstShots = Integer.parseInt(made[0]);
                leastShotsTotal = Integer.toString(Integer.parseInt(made[0]) + Integer.parseInt(made[1]) + Integer.parseInt(made[2]) + Integer.parseInt(made[3]));
                leastShotsDate = items.get(i).date;
                leastShotsDate = items.get(i).date;
                leastShotsDate = leastShotsDate.split(" ")[0] + " " + leastShotsDate.split(" ")[1].substring(0,2);
            }
        }
        leastShots = Integer.toString(worstShots);
    }

    public void calculate_avgTime() {
        int sumTime = 0;
        String time = "";
        for (int i = 0; i < items.size(); i++) {
            time = items.get(i).time;
            sumTime += Integer.parseInt(time);
        }
        int avgShotsInt = sumTime / items.size();
        avgPracticeTime = Integer.toString(avgShotsInt);
    }

    public void calculate_mostTime() {
        int bestTime = 0;
        for (int i = 0; i < items.size(); i++) {
            String time = items.get(i).time;
            if (Integer.parseInt(time) > bestTime) {
                bestTime = Integer.parseInt(time);
            }
        }
        mostPracticeTime = Integer.toString(bestTime);
    }


    public void calculate_leastTime() {
        int leastTime = 6969;
        for (int i = 0; i < items.size(); i++) {
            String time = items.get(i).time;
            if (Integer.parseInt(time) < leastTime) {
                leastTime = Integer.parseInt(time);
            }
        }
        leastPracticeTime = Integer.toString(leastTime);
    }



}
