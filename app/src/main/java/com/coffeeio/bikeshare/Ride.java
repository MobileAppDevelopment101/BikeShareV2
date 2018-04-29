package com.coffeeio.bikeshare;

import android.location.Location;

import java.util.Locale;
import java.util.UUID;

import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Ride {

    @PrimaryKey
    @Required
    private String id;
    @Required
    private String bikeId;
    @Required
    private double cost;
    @Required
    private Location startLocation;
    @Required
    private String startTime;

    private Location endLocation;
    private String endTime;

    public Ride() {
        id = UUID.randomUUID().toString();
    }
    public String getId() {
        return id;
    }
}
