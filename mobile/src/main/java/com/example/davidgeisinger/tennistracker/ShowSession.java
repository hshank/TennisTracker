package com.example.davidgeisinger.tennistracker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import android.text.Html;


public class ShowSession extends AppCompatActivity {

    Button back_button;
    TextView shotsInText;
    TextView shotsLongText;
    TextView shotsWideText;
    TextView shotsNetText;
    TextView durationText;

    String practiceTime;
    int shotsMade;
    int shotsLong;
    int shotsWide;
    int shotsNet;
    int shotsTotal;

    GraphView graph;
    String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
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

        String [] str_arr = message.split("!");
        final String stroke = str_arr[2];
        if (stroke.equals("f")){
            color = "#218A6A";
            back_button.setBackgroundColor(Color.parseColor(color));

        }
        else if (stroke.equals("b")){
            color = "#34A17B";
            back_button.setBackgroundColor(Color.parseColor(color));
        }
        else if (stroke.equals("s")){
            color = "#78AF62";
            back_button.setBackgroundColor(Color.parseColor(color));
        }
        else {
            color = "#B4B64D";
            back_button.setBackgroundColor(Color.parseColor(color));
        }
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(color));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        setTitle(str_arr[0]);


        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShowSession.this, HomeScreen.class);
                //intent.setAction("passing_stroke");
                intent.putExtra("stroke", stroke);
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

        graph = (GraphView) findViewById(R.id.bargraph);

        makeChart();
        changeTexts();


    }

    //parse passed string to get array of shots made and missed
    public int[] getShots(String message) {
        String [] arr = message.split("!");
        String [] stats = arr[1].split("\\$");
        int [] answer = new int [4];
        answer[0] = Integer.parseInt(stats[0]);
        answer[1] = Integer.parseInt(stats[1]);
        answer[2] = Integer.parseInt(stats[2]);
        answer[3] = Integer.parseInt(stats[3]);

        return answer;
    }

    //parse passed string to get duration
    public String getTime(String message) {
        String [] arr = message.split("!");

        return arr[3];
    }

    public void changeTexts() {
        shotsInText.setText(Html.fromHtml("    <b>Successful: </b> " + Integer.toString(shotsMade) + "/" + Integer.toString(shotsTotal) + " shots"));
        shotsLongText.setText(Html.fromHtml("    <b>Long: </b> " + Integer.toString(shotsLong) + "/" + Integer.toString(shotsTotal) + " shots"));
        shotsWideText.setText(Html.fromHtml("    <b>Wide: </b> " + Integer.toString(shotsWide) + "/" + Integer.toString(shotsTotal) + " shots"));
        shotsNetText.setText(Html.fromHtml("    <b>Net: </b> " + Integer.toString(shotsNet) + "/" + Integer.toString(shotsTotal) + " shots"));
        durationText.setText(Html.fromHtml("    <b>Length of Practice: </b> " + practiceTime + " minutes"));
    }

    //make the chart based on the shots statistics
    public void makeChart() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, shotsMade),
                new DataPoint(1, shotsLong),
                new DataPoint(2, shotsWide),
                new DataPoint(3, shotsNet)
        });
        graph.addSeries(series);
        graph.setTitle("Practice Overview");
        graph.setTitleTextSize(100);
        Viewport viewPort = graph.getViewport();
        viewPort.setYAxisBoundsManual(true);
        viewPort.setMinY(0);
        viewPort.setMaxY(shotsMade + shotsLong + shotsWide + shotsNet);
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.parseColor(color));
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.parseColor(color);
            }
        });

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"made", "long", "wide", "net"});
        //staticLabelsFormatter.setVerticalLabels(new String[]{"low", "middle", "high"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


    }
}
