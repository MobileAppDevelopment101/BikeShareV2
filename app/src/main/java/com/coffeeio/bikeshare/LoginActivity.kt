package com.coffeeio.bikeshare

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import io.realm.ObjectServerError
import io.realm.SyncCredentials
import io.realm.SyncUser

import com.coffeeio.bikeshare.Constants.AUTH_URL
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mUsernameEditText: EditText
    private lateinit var mPasswordEditText: EditText

    lateinit var realm : Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)

        realm = Database().getRealm(this)
        // Set up the login form.
        mPasswordEditText = findViewById(R.id.password)
        mUsernameEditText = findViewById(R.id.username)
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener { view ->
            val password =  mPasswordEditText.text.toString().trim()
            val username =  mUsernameEditText.text.toString().trim()
            val hash = HashUtils().hashString("SHA-512", password)
            val result : User? = realm.where(User::class.java).equalTo("username", username).equalTo("password", hash).findFirst()
            Log.d("myTag", "pass: $password")
            Log.d("myTag", "hash: $hash")
            Log.d("myTag", "$result")
            if (result != null) {
                goToMainActivity(result.getId())
            } else {
                Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show()
            }

        }
        fab.setOnClickListener { view ->
            realm.executeTransaction(object : Realm.Transaction {
                override fun execute(realm: Realm) {
                    realm.insertOrUpdate(User("tom", HashUtils().hashString("SHA-512", "1")))
                }
            })
        }
    }

    private fun goToMainActivity(userid: String) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("userid", userid)
        startActivity(intent)
    }
}
