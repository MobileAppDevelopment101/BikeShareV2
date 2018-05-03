package com.coffeeio.bikeshare

import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_endride.*

class StartRide : AppCompatActivity() {
    lateinit var realm : Realm
    lateinit var session: SessionStorage
    lateinit var findBikeSpinner : Spinner
    lateinit var bikes : RealmResults<Bike>
    lateinit var priceTypeTxt : TextView

    var selectedBikeUid = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startride)

        session = SessionStorage.get(this)
        realm = Database().getRealm(this)
        bikes = realm.where(Bike::class.java).equalTo("inUse", false).findAll()

        findBikeSpinner = findViewById(R.id.find_bike)
        val directionButton = findViewById<Button>(R.id.directions)
        val showBikeButton = findViewById<Button>(R.id.show_bike)
        val startRideButton = findViewById<Button>(R.id.start_ride)
        priceTypeTxt = findViewById(R.id.price_type)

        directionButton.setOnClickListener { view ->
            // Validate
            if (selectedBikeUid == "") return@setOnClickListener
            val bike = realm.where(Bike::class.java).equalTo("id", selectedBikeUid).findFirst()
            if (bike == null) return@setOnClickListener

            val gmmIntentUri = Uri.parse("google.navigation:q=" + bike.lastLatitude + "," + bike.lastLongtitude)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            startActivity(mapIntent)
        }
        showBikeButton.setOnClickListener { view ->
            // Validate
            if (selectedBikeUid == "") return@setOnClickListener
            val bike = realm.where(Bike::class.java).equalTo("id", selectedBikeUid).findFirst()
            if (bike == null) return@setOnClickListener

            val intent = Intent(this@StartRide, ShowBike::class.java)
            intent.putExtra("selectedBikeUid", selectedBikeUid);
            startActivity(intent)
        }
        startRideButton.setOnClickListener { view ->
            if (selectedBikeUid == "") return@setOnClickListener
            val bike = realm.where(Bike::class.java).equalTo("id", selectedBikeUid).equalTo("inUse", false).findFirst()
            if (bike == null) return@setOnClickListener

            val ride = Ride()
            ride.bikeId = selectedBikeUid
            ride.userId = session.userid
            ride.startTime = System.currentTimeMillis() / 1000L
            ride.startLatitude = 0.0
            ride.startLongtitude = 0.0

            realm.executeTransaction { realm ->
                bike.isInUse = true
                realm.insertOrUpdate(ride)
            }

            Toast.makeText(this, "Your ride has started, have fun", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@StartRide, MainActivity::class.java)
            startActivity(intent)
        }

        // Loop to update distance to bikes.
        if (updateSpinner()) {
            var t = 0
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    t++
                    Log.d("myTag", "time --> " + t)
                    if (updateSpinner())
                        handler.postDelayed(this, 1000)
                }
            }, 2000)
        }

    }

    private fun updateSpinner() : Boolean {
        bikes = realm.where(Bike::class.java).equalTo("inUse", false).findAll()
        var spinnerBikes: MutableList<String> = mutableListOf<String>()

        if (selectedBikeUid != "") {
            val bike = realm.where(Bike::class.java).equalTo("id", selectedBikeUid).findFirst()
            if (bike != null) {
                addBikeToSpinner(bike, spinnerBikes)
            } else spinnerBikes.add("")
        } else spinnerBikes.add("")

        if (bikes.count() == 0) {
            Toast.makeText(this, "Sorry no bikes available, wait till bikes become available", Toast.LENGTH_LONG).show()
            val intent = Intent(this@StartRide, MainActivity::class.java)
            startActivity(intent)
            return false
        }
        bikes.forEach { bike ->
            Log.d("myTag", "$bike")
            addBikeToSpinner(bike, spinnerBikes)
        }

        findBikeSpinner.setOnItemSelectedListener(itemSelectedListener)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerBikes)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        findBikeSpinner.setAdapter(aa)

        return true
    }

    private fun getDistance(bike: Bike) : Float {
        var distance = -1f
        val mLocation = session.location
        if (mLocation != null) {
            val bLoc = Location("bikeLocation")
            bLoc.latitude = bike.lastLatitude
            bLoc.longitude = bike.lastLongtitude
            distance = mLocation.distanceTo(bLoc);
        }
        return distance
    }
    private fun addBikeToSpinner(bike: Bike, list: MutableList<String>) {
        val distance = getDistance(bike)
        if (distance != -1f) list.add(bike.name + String.format(" : %.1fm", distance))
        else list.add(bike.name)
    }

    private fun setPriceType() {
        if (selectedBikeUid == null) return
        val bike = realm.where(Bike::class.java).equalTo("id", selectedBikeUid).findFirst()
        if (bike == null) return

        val bikeType = BikeTypes().getTypeFromId(bike!!.typeid)
        priceTypeTxt.text = "" + bike!!.price + "kr/min / "+ bikeType
    }

    private val itemSelectedListener : AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
            Log.d("myTag", "Selected pos $position , id: $id")
            if (position == 0) {
                return
            }

            val bike = bikes[position - 1]
            if (bike != null) selectedBikeUid = bike.id
            setPriceType()
        }
        override fun onNothingSelected(arg0: AdapterView<*>) {

        }
    }

}