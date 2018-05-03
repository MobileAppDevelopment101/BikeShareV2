package com.coffeeio.bikeshare

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_createbike.*
import java.io.ByteArrayOutputStream

class CreateBike : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var bikeImageView : ImageView
    private lateinit var imageBitmap : Bitmap
    private var bikeType = 1 // Start at first typeid
    lateinit var realm : Realm
    var name : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createbike)

        val session = SessionStorage.get(this)
        if (session.userid == "") {
            val intent = Intent(this@CreateBike, LoginActivity::class.java)
            startActivity(intent)
        }
        realm = Database().getRealm(this)


        val createBikeSpinner = findViewById<Spinner>(R.id.type_spinner)
        val priceEditText = findViewById<EditText>(R.id.price)
        val takeImageButton = findViewById<Button>(R.id.take_image)
        val createBikeButton = findViewById<Button>(R.id.create_bike)
        bikeImageView = findViewById<ImageView>(R.id.bike_image)

        var list_of_items = BikeTypes().getList()

        createBikeSpinner.setOnItemSelectedListener(itemSelectedListener)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        createBikeSpinner.setAdapter(aa)

        takeImageButton.setOnClickListener { view ->
            dispatchTakePictureIntent()
        }
        createBikeButton.setOnClickListener { view ->
            val price = priceEditText.text.toString().trim()
            Log.d("myTag", "$price")

            if (! validateType(bikeType)) return@setOnClickListener
            if (! validatePrice(price)) return@setOnClickListener

            if (! ::imageBitmap.isInitialized) {
                Toast.makeText(this, "Photo not set", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (! validatePhoto(imageBitmap)) return@setOnClickListener

            val myLoc = session.location
            if (myLoc == null) {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }

            var b = Bike()
            b.typeid = bikeType
            b.price = java.lang.Double.parseDouble(price)
            b.userid = session.userid
            b.name = name

            val stream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            val image = stream.toByteArray()
            b.picture = image

            Log.d("bikeCreate", "" + bikeImageView.width + " : " + bikeImageView.height)

            b.lastLatitude = myLoc.latitude
            b.lastLongtitude = myLoc.longitude

            realm.executeTransaction(object : Realm.Transaction {
                override fun execute(realm: Realm) {
                    realm.insertOrUpdate(b)
                }
            })
            Toast.makeText(this, "Bike created", Toast.LENGTH_SHORT).show()
            Log.d("myTag", "$b")
            val intent = Intent(this@CreateBike, MainActivity::class.java)
            startActivity(intent)
        }

        val handler = Handler()
        val url = "https://randomuser.me/api/"
        FetchName(applicationContext).execute(url)
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (session.genName == "") {
                    FetchName(applicationContext).execute(url)
                    handler.postDelayed(this, 1000)
                } else {
                    name = session.genName
                    session.genName = ""
                    bike_name.text = name
                }
            }
        }, 1000)
    }

    private fun validateType(bikeType : Int) : Boolean {
        if (bikeType == -1) {
            return false
        }
        return true
    }
    private fun validatePrice(price : String) : Boolean {
        if (price.length <= 0) {
            Toast.makeText(this, "Price not set", Toast.LENGTH_SHORT).show()
            return false
        }
        try {
            val priceValue = java.lang.Double.parseDouble(price)
        } catch (ex : Exception) {
            Toast.makeText(this, "Price is not a valid number", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
    private fun validatePhoto(bitmap : Bitmap) : Boolean {
        if (bitmap == null) {
            Toast.makeText(this, "Photo not set", Toast.LENGTH_SHORT).show()
            return false
        }
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        return true
    }

    private val itemSelectedListener : AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
            Log.d("myTag", "Selected pos $position , id: $id")
            bikeType = BikeTypes().getTypeId(position)
        }
        override fun onNothingSelected(arg0: AdapterView<*>) {

        }
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
            imageBitmap = extras!!.get("data") as Bitmap
            bikeImageView.setImageBitmap(imageBitmap)
        }
    }
}