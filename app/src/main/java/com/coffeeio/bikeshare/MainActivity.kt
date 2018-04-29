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
import io.realm.*

import com.coffeeio.bikeshare.Constants.REALM_BASE_URL
import com.coffeeio.bikeshare.R.id.toolbar
import io.realm.Realm.setDefaultConfiguration
import io.realm.RealmConfiguration



class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var mImageView: ImageView
    lateinit var thetext: TextView
    lateinit var locationManager: LocationManager
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Database().getRealm(this)


        val intent = getIntent();
        val userid = intent.getStringExtra("userid")
        Log.d("myTag","userid: $userid")


        val findRideButton = findViewById<Button>(R.id.find_ride)
        val endRideButton = findViewById<Button>(R.id.end_ride)
        val myRidesButton = findViewById<Button>(R.id.my_rides)
        val myAccount = findViewById<Button>(R.id.my_account)

        findRideButton.setOnClickListener { view ->

        }
        endRideButton.setOnClickListener { view ->

        }
        myRidesButton.setOnClickListener { view ->

        }
        myAccount.setOnClickListener { view ->

        }
        fab.setOnClickListener { view ->

        }


        var items = realm.where(Bike::class.java).findAll();

        Log.d("myTag", "$items")

        ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1);

        mImageView = findViewById(R.id.imageView)
        thetext = findViewById(R.id.thetext)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        /*
        fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show()
            //dispatchTakePictureIntent()
        }
        fab.setOnClickListener { view ->

            realm.executeTransaction(object : Realm.Transaction {
                override fun execute(realm: Realm) {
                    realm.insertOrUpdate(Bike())
                }
            })
            var items2 = realm.where(Bike::class.java).findAll();
            Log.d("myTag", "$items2")
            try {
                // Request location updates
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener);
            } catch(ex: SecurityException) {
                Log.d("myTag", "Security Exception, no location available");
                Log.d("myTag",ex.toString())

            }
        }
        */
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

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            thetext.setText("" + location.longitude + ":" + location.latitude);
            Log.d("myTag","" + location.longitude + ":" + location.latitude )
            var l = Location("")
                l.latitude = 55.643060
                l.longitude = 12.580261
            Log.d("myTag","${l.distanceTo(location)}" )
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data.extras
            val imageBitmap = extras!!.get("data") as Bitmap
            mImageView.setImageBitmap(imageBitmap)
        }
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
