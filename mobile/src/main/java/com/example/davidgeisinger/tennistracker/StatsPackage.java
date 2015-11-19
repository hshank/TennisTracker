package com.example.davidgeisinger.tennistracker;

/**
 * Created by davidgeisinger on 11/8/15.
 */
public class StatsPackage {

    public String date;
    public String stats;
    public String time;
    public String stroke;


    public StatsPackage(String first, String second, String third, String fourth) {
        date = first;
        stats = second;
        stroke = third;
        time = fourth;

    }

}
