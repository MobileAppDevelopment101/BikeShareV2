package com.coffeeio.bikeshare

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log

class MyLocation : LocationListener {
    lateinit var myLocation : Location

    fun hasLocation() : Boolean {
        if (::myLocation.isInitialized) {
            return true
        }

        return false
    }
    fun getLocation() : Location {
        return myLocation
    }

    override fun onLocationChanged(location: Location) {
        myLocation = location
    }
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}