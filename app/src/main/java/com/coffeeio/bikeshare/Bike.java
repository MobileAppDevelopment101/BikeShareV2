package com.coffeeio.bikeshare;

import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.RequiresPermission;

import java.util.Locale;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Bike extends RealmObject {

    @PrimaryKey
    @Required
    private String id;
    @Required
    private String name;
    @Required
    private String userid;
    @Required
    private byte[] picture;

    private double lastLatitude;
    private double lastLongtitude;
    private int typeid;
    private double price;
    private boolean inUse;

    public Bike() {
        id = UUID.randomUUID().toString();
        name = "Something";
        inUse = false;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }


    public double getLastLongtitude() {
        return lastLongtitude;
    }

    public void setLastLongtitude(double lastLongtitude) {
        this.lastLongtitude = lastLongtitude;
    }

    public double getLastLatitude() {
        return lastLatitude;
    }

    public void setLastLatitude(double lastLatitude) {
        this.lastLatitude = lastLatitude;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
}
