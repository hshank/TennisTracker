package com.example.davidgeisinger.tennistracker;

/**
 * Created by Julian on 12/1/2015.
 */
public interface StrokeUpdateListener {
    public void sendProgress(double progress);
    // Need to send Progress from the watch interface to phone's activityListener
}
