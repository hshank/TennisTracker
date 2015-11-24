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

    public String toString() {
        String [] arr = stats.split("\\$");
        int made = Integer.parseInt(arr[0]);
        int longg = Integer.parseInt(arr[1]);
        int wide = Integer.parseInt(arr[2]);
        int net = Integer.parseInt(arr[3]);
        int total = made + wide + longg + net;
        return date + "   " + Integer.toString(made) + " out of " + Integer.toString(total) + " shots made";
    }

}
