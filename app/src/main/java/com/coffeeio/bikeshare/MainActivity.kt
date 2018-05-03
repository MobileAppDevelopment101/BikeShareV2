package com.coffeeio.bikeshare

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.widget.ImageView
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import io.realm.*

import com.coffeeio.bikeshare.Constants.REALM_BASE_URL
import io.realm.Realm.setDefaultConfiguration
import io.realm.RealmConfiguration



class MainActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var session : SessionStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Database().getRealm(this)
        session = SessionStorage.get(this)

        val userid = session.userid
        Log.d("myTag","userid: $userid")
        Log.d("myTag","userid: $userid")


        val findRideButton = findViewById<Button>(R.id.find_ride)
        val endRideButton = findViewById<Button>(R.id.end_ride)
        val myRidesButton = findViewById<Button>(R.id.my_rides)
        val myAccount = findViewById<Button>(R.id.my_account)

        findRideButton.setOnClickListener { view ->
            // Check user doesn't already have active ride.
            val ride = realm.where(Ride::class.java).equalTo("userId", session.userid).equalTo("isEnded",false).findFirst()
            if (ride != null) {
                Toast.makeText(this, "You can only have 1 ride active at a time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this@MainActivity, StartRide::class.java)
            startActivity(intent)
        }
        endRideButton.setOnClickListener { view ->
            // Check user has an active ride.
            val ride = realm.where(Ride::class.java).equalTo("userId", session.userid).equalTo("isEnded",false).findFirst()
            if (ride == null) {
                Toast.makeText(this, "No ride is active", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this@MainActivity, EndRide::class.java)
            startActivity(intent)
        }
        myRidesButton.setOnClickListener { view ->
            val bikes = realm.where(Bike::class.java).equalTo("userid", session.userid)
            if (bikes == null) {
                Toast.makeText(this, "Sorry, you don't have any bikes registered", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this@MainActivity, MyBikes::class.java)
            startActivity(intent)
        }
        myAccount.setOnClickListener { view ->

        }
        fab.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, CreateBike::class.java)
            startActivity(intent)
        }


        var items = realm.where(Bike::class.java).findAll();

        Log.d("myTag", "$items")

        //locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private fun setUpRealm(): RealmResults<Bike> {
        val configuration = SyncConfiguration.Builder(
                SyncUser.current(),
                REALM_BASE_URL + "/items").build()

        realm = Realm.getInstance(configuration)
        return realm
                .where(Bike::class.java) // analogous to Item.class
                .findAllAsync()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_logout -> {
                session.userid = ""
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
