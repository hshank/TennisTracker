package com.example.davidgeisinger.tennistracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    private ListView lv;
    ArrayAdapter<String> arrayAdapter;
    List<String> your_array_list;
    ImageButton forehandButton;
    ImageButton backhandButton;
    ImageButton serveButton;
    ImageButton volleyButton;
    Button seeOverviewButton;

    MyDBHandler dbHandler = new MyDBHandler(this);

    String whichStroke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);



        whichStroke = "f";
        setTheSwingListeners();
        setTheOverviewListener();

        lv = (ListView) findViewById(R.id.listy);

        your_array_list = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);


        lv.setAdapter(arrayAdapter);

        String message;
        Bundle mybundle = getIntent().getExtras();
        if (mybundle != null) {
            if (mybundle.getString("phone_data") != null) {
                message = mybundle.getString("phone_data");
                putInDB(message, dbHandler);
            }
            if (mybundle.getString("stroke") != null) {
                Log.d("GETTINGTOHERE", mybundle.getString("stroke"));
                whichStroke = mybundle.getString("stroke");
            }

        }

       /* StatsPackage addthis = new StatsPackage("November 23rd", "5$6$9$6", "b", "56");
        Log.d("Start", addthis.time);
        dbHandler.addEntry(dbHandler, addthis);
        Log.d("Start", "HILLEZ");

        StatsPackage check_this = dbHandler.findEntry(addthis);
       Log.d("Chekcing", check_this.time);*/

        populateListView(whichStroke, dbHandler);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Object item = parent.getItemAtPosition(position);
                String real_item = (String) item;

                Intent intent = new Intent(HomeScreen.this, ShowSession.class);


                intent.putExtra("string_passed", real_item);
                startActivity(intent);

            }


        });
    }

    public void populateListView(String stroke, MyDBHandler db) {
        ArrayList<StatsPackage> items = db.findMany(stroke);
        arrayAdapter.clear();
        if (items.get(0) != null) {
            Log.d("MYSIZE", Integer.toString(items.size()));
            for(int i = 0; i < items.size(); i++) {
                Log.d("whatimentering", items.get(i).date + items.get(i).stats + items.get(i).stroke + items.get(i).time);
                arrayAdapter.insert(items.get(i).date + "!" + items.get(i).stats + "!" + items.get(i).stroke + "!" + items.get(i).time, i);
            }
        } else {
            // do something to tell them there are no entries
        }
        Log.d("FINISHED", "OK");

        // we must iterate over all entries in database that correspond to the stroke
        //we are on
    }

    public void setTheSwingListeners() {
        forehandButton = (ImageButton) findViewById(R.id.forehandPic);
        backhandButton = (ImageButton) findViewById(R.id.backhandPic);
        serveButton = (ImageButton) findViewById(R.id.servePic);
        volleyButton = (ImageButton) findViewById(R.id.volleyPic);

        // if they choose forehand
        forehandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichStroke = "f";
                populateListView(whichStroke, dbHandler);

            }
        });
        // if they choose backhand
        backhandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichStroke = "b";
                populateListView(whichStroke, dbHandler);

            }
        });

        // if they choose serve
        serveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichStroke = "s";
                populateListView(whichStroke, dbHandler);
            }
        });

        // if they choose volley
        volleyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichStroke = "v";
                populateListView(whichStroke, dbHandler);
            }
        });

    }

    public void setTheOverviewListener() {
        seeOverviewButton = (Button) findViewById(R.id.overviewButton);

        // set its listener
        seeOverviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String superlongstr = "";
                for (int i = 0; i < your_array_list.size(); i++) {
                    superlongstr += your_array_list.get(i) + "$";
                }*/

                // This makes it so clicking to see an overview without data will not crash, we
                // have to choose what to do here
                if (your_array_list.size() == 0) {
                    return;
                }
                Intent intent = new Intent(HomeScreen.this, OverviewActivity.class);


                intent.putExtra("string_to_overview", whichStroke);
                startActivity(intent);
            }
        });
    }



    public void putInDB(String message, MyDBHandler dbHandler) {
        //parse message
        String [] arr = message.split("!");
        StatsPackage temp_sp = new StatsPackage(arr[0], arr[1], arr[2], arr[3]);
        dbHandler.addEntry(dbHandler, temp_sp);

        //actually populate the DB
    }


}
