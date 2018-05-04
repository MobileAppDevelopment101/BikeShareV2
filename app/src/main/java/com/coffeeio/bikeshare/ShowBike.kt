package com.coffeeio.bikeshare

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import io.realm.Realm

class ShowBike : AppCompatActivity() {
    lateinit var realm : Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showbike)

        val session = SessionStorage.get(this)
        if (session.userid == "") {
            val intent = Intent(this@ShowBike, LoginActivity::class.java)
            startActivity(intent)
        }
        realm = Database().getRealm(this)

        val extra = intent.extras
        if (extra == null) {
            finish()
        }

        val bikeId = extra.get("selectedBikeUid") as String
        if (bikeId == null ) {
            finish()
        }
        val bike = realm.where(Bike::class.java).equalTo("id", bikeId).findFirst()
        if (bike == null) {
            finish()
        }

        val bikeNameTxt = findViewById<TextView>(R.id.bike_name)
        val bikePriceTxt = findViewById<TextView>(R.id.bike_price)
        val bikeTypeTxt = findViewById<TextView>(R.id.bike_type)
        val bikeImage = findViewById<ImageView>(R.id.bike_image)

        bikeNameTxt.text = bike!!.name
        bikePriceTxt.text = "" + bike!!.price
        val bikeType = BikeTypes().getTypeFromId(bike!!.typeid)
        bikeTypeTxt.text = bikeType

        val imageBytes = bike!!.picture
        val bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size);
        bikeImage.setImageBitmap(bmp)
    }
}