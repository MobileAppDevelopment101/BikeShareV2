package com.coffeeio.bikeshare

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.Toast
import io.realm.*

class MainActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var session : SessionStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Database().getRealm(this)
        session = SessionStorage.get(this)
        if (session.userid == "") {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        val findRideButton = findViewById<Button>(R.id.find_ride)
        val endRideButton = findViewById<Button>(R.id.end_ride)
        val myRidesButton = findViewById<Button>(R.id.my_rides)

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
        fab.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, CreateBike::class.java)
            startActivity(intent)
        }


        val user = realm.where(User::class.java).equalTo("id", session.userid).findFirst()
        account_balance.text = "" + user?.balance + " kr"
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
