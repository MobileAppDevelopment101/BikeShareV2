package com.coffeeio.bikeshare

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_endride.*

class EndRide : AppCompatActivity() {
    lateinit var realm : Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_endride)

        val session = SessionStorage.get(this)
        if (session.userid == "") {
            val intent = Intent(this@EndRide, LoginActivity::class.java)
            startActivity(intent)
        }
        realm = Database().getRealm(this)

        val ride = realm.where(Ride::class.java).equalTo("userId", session.userid).equalTo("isEnded", false).findFirst()
        if (ride == null) {
            Toast.makeText(this, "Something when wrong", Toast.LENGTH_SHORT).show()
            finish()
        }
        val bike =  realm.where(Bike::class.java).equalTo("id", ride!!.bikeId).findFirst()
        val endRideButton = findViewById<Button>(R.id.end_ride)

        endRideButton.setOnClickListener { view ->
            val currentTime = System.currentTimeMillis() / 1000L
            val currentDiff = (currentTime - ride!!.startTime)
            val cost = getCost(bike!!.price, currentDiff)

            val location = session.location
            if (location == null) {
                Toast.makeText(this, "Location not found, please wait a bit and try again", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val renter = realm.where(User::class.java).equalTo("id", session.userid).findFirst()
            val owner = realm.where(User::class.java).equalTo("id", bike.userid).findFirst()

            realm.executeTransaction { realm ->
                    ride.endTime = currentTime
                    ride.cost = cost
                    ride.endLatitude = location.latitude
                    ride.endLongtitude = location.longitude
                    ride.isEnded = true

                    bike.lastLongtitude = location.longitude
                    bike.lastLatitude = location.latitude
                    bike.isInUse = false

                    renter?.balance = renter!!.balance - cost.toDouble()
                    owner?.balance = owner!!.balance + (cost).toDouble()
            }

            Toast.makeText(this, "Ride finish, money has been deduced from your account", Toast.LENGTH_LONG).show()
            val intent = Intent(this@EndRide, MainActivity::class.java)
            startActivity(intent)
        }
        // Loop to update distance to bikes.
        if (updateTime(ride, bike)) {
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    if (updateTime(ride, bike))
                        handler.postDelayed(this, 1000)
                }
            }, 1000)
        }
    }

    private fun updateTime(ride : Ride?, bike: Bike?) : Boolean {
        val currentTime = System.currentTimeMillis() / 1000L
        val currentDiff = (currentTime - ride!!.startTime)
        val currentMin = Math.floor(currentDiff / 60.0)
        val currentSec = currentDiff - (currentMin * 60.0)

        time.text = "" + currentMin.toInt() + ":" + currentSec.toInt()
        price.text = "" + getCost(bike!!.price, currentDiff) + "kr"

        return true
    }

    private fun getCost(price : Double, seconds: Long) : Double {
        return price * Math.ceil(seconds / 60.0)
    }


}