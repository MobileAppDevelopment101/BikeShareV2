package com.coffeeio.bikeshare

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.*
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_my_bikes.*
import java.text.SimpleDateFormat

class MyBikes : AppCompatActivity() {
    lateinit var realm : Realm
    lateinit var session: SessionStorage
    lateinit var bikesSpinner : Spinner
    lateinit var bikes : RealmResults<Bike>

    val rideUsers: ArrayList<String> = ArrayList()
    val rideTime: ArrayList<String> = ArrayList()
    val rideDuration: ArrayList<String> = ArrayList()
    val rideCost: ArrayList<String> = ArrayList()

    var selectedBikeUid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bikes)

        session = SessionStorage.get(this)
        if (session.userid == "") {
            val intent = Intent(this@MyBikes, LoginActivity::class.java)
            startActivity(intent)
        }
        realm = Database().getRealm(this)

        bikesSpinner = findViewById(R.id.bikes_spinner)
        bikes = realm.where(Bike::class.java).equalTo("userid", session.userid).findAll()
        val user = realm.where(User::class.java).equalTo("id", session.userid).findFirst()
        if (user == null) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            finish()
        }

        initSpinner()

        rides_view.layoutManager = LinearLayoutManager(this)
        rides_view.adapter = RidesAdapter(rideUsers, rideTime, rideDuration, rideCost, this)
    }

    private fun initSpinner() : Boolean {
        var spinnerBikes: MutableList<String> = mutableListOf<String>()

        if (selectedBikeUid != "") {
            val bike = realm.where(Bike::class.java).equalTo("id", selectedBikeUid).findFirst()
            if (bike != null) {
                spinnerBikes.add(bike.name)
            } else spinnerBikes.add("")
        } else spinnerBikes.add("")

        if (bikes.count() == 0) {
            Toast.makeText(this, "Sorry no bikes available", Toast.LENGTH_LONG).show()
            val intent = Intent(this@MyBikes, MainActivity::class.java)
            startActivity(intent)
            return false
        }
        bikes.forEach { bike ->
            spinnerBikes.add(bike.name)
        }

        bikesSpinner.setOnItemSelectedListener(itemSelectedListener)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerBikes)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bikesSpinner.setAdapter(aa)

        return true
    }

    private fun updateList(rides : RealmResults<Ride>) {
        rideUsers.clear()
        rideTime.clear()
        rideDuration.clear()
        rideCost.clear()

        rides.forEach { ride ->
            val user = realm.where(User::class.java).equalTo("id", ride.userId).findFirst()
            rideUsers.add(user!!.username)

            val time = ride.startTime * 1000
            val df = java.util.Date(time)
            val vv = SimpleDateFormat("dd/MM - yy HH:mm").format(df)
            rideTime.add(vv)

            if (ride.isEnded) {
                val minutes = Math.ceil((ride.endTime - ride.startTime) / 60.0).toInt()
                rideDuration.add("" + minutes + " min")
                rideCost.add("" + ride.cost + " kr")
            } else {
                rideDuration.add("-")
                rideCost.add("-")
            }
        }

        rides_view.adapter.notifyDataSetChanged()
    }

    private val itemSelectedListener : AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
            if (position == 0) {
                return
            }

            val bike = bikes[position - 1]
            if (bike != null) selectedBikeUid = bike.id

            val rides = realm.where(Ride::class.java).equalTo("bikeId", selectedBikeUid).findAll()
            updateList(rides)
        }
        override fun onNothingSelected(arg0: AdapterView<*>) {

        }
    }
}