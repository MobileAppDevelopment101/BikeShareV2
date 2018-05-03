package com.coffeeio.bikeshare;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

public class SessionStorage {
    private static SessionStorage sSessionStorage;
    private String userid = "";
    private LocationManager locationManager;
    private MyLocation loc;
    private String genName = "";

    private SessionStorage(Context context) {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        loc = new MyLocation();
        if ( ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if ( ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, loc);
            } catch (Exception ex) {
                Log.d("myTag", ex.toString());
            }
        }
    }
    private SessionStorage() {
    }
    public static SessionStorage get(Context context) {
        if (sSessionStorage == null) {
            sSessionStorage = new SessionStorage(context);
        }
        return sSessionStorage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Location getLocation() {
        if (! loc.hasLocation()) return null;
        return loc.getLocation();
    }

    public String getGenName() {
        return genName;
    }

    public void setGenName(String genName) {
        this.genName = genName;
    }
}
