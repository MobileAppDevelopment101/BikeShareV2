package com.coffeeio.bikeshare;

import android.location.Location;

import java.util.Locale;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Ride extends RealmObject {

    @PrimaryKey
    @Required
    private String id;
    @Required
    private String bikeId;
    @Required
    private String userId;

    private double startLongtitude;
    private double endLongtitude;
    private double startLatitude;
    private double endLatitude;

    private long startTime;
    private long endTime;

    private double cost;
    private boolean isEnded;

    public Ride() {
        id = UUID.randomUUID().toString();
        isEnded = false;
    }
    public String getId() {
        return id;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getStartLongtitude() {
        return startLongtitude;
    }

    public void setStartLongtitude(double startLongtitude) {
        this.startLongtitude = startLongtitude;
    }

    public double getEndLongtitude() {
        return endLongtitude;
    }

    public void setEndLongtitude(double endLongtitude) {
        this.endLongtitude = endLongtitude;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }
}
