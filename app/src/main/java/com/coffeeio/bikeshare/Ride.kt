package com.coffeeio.bikeshare

import io.realm.RealmObject
import java.util.*

class Ride{
    private var id = ""
    private var startLocation = ""
    private var endLocation = ""
    private var bikeId = ""
    private var cost = 0
    private var startTime = 0L
    private var endTime = 0L

    constructor() {
        id =  UUID.randomUUID().toString()
    }

    fun startRide(_location: String, _startTime: String, _bikeId: String) {
        startLocation = _location
        startTime = System.currentTimeMillis() / 1000L
        bikeId = _bikeId
    }
}
