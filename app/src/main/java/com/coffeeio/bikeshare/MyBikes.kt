package com.coffeeio.bikeshare

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_my_bikes.*

class MyBikes : AppCompatActivity() {
    lateinit var realm : Realm
    lateinit var session: SessionStorage
    lateinit var bikesSpinner : Spinner
    lateinit var bikes : RealmResults<Bike>

    var selectedBikeUid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bikes)

        session = SessionStorage.get(this)
        realm = Database().getRealm(this)


        val balanceTxt = findViewById<TextView>(R.id.account_balance)
        bikesSpinner = findViewById(R.id.bikes_spinner)


        bikes = realm.where(Bike::class.java).equalTo("userid", session.userid).findAll()

        val user = realm.where(User::class.java).equalTo("id", session.userid).findFirst()
        if (user == null) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            finish()
        }
        Log.d("bikeMy", "$user")

        balanceTxt.text = "" + user!!.balance + " kr"
        initSpinner()

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
            Log.d("myTag", "$bike")
            spinnerBikes.add(bike.name)
        }

        bikesSpinner.setOnItemSelectedListener(itemSelectedListener)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerBikes)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        bikesSpinner.setAdapter(aa)

        return true
    }

    private val itemSelectedListener : AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
            Log.d("myTag", "Selected pos $position , id: $id")
            if (position == 0) {
                return
            }

            val bike = bikes[position - 1]
            if (bike != null) selectedBikeUid = bike.id

            updateList(selectedBikeUid)
        }
        override fun onNothingSelected(arg0: AdapterView<*>) {

        }
    }

    private fun updateList(bikeId : String) {
        val rides = realm.where(Ride::class.java).equalTo("bikeId", bikeId).findAll()


    }

}