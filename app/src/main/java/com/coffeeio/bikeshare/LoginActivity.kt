package com.coffeeio.bikeshare

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.realm.Realm

class LoginActivity : AppCompatActivity() {

    private lateinit var mUsernameEditText: EditText
    private lateinit var mPasswordEditText: EditText

    lateinit var realm : Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val session = SessionStorage.get(this)
        if (session.userid != "") {
            goToMainActivity(session.userid)
            return
        }

        realm = Database().getRealm(this)
        // Set up the login form.
        mPasswordEditText = findViewById(R.id.password)
        mUsernameEditText = findViewById(R.id.username)
        val loginButton = findViewById<Button>(R.id.login_button)
        val createButton = findViewById<Button>(R.id.create_button)
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
        createButton.setOnClickListener { view ->
            val username =  mUsernameEditText.text.toString().trim()
            val password =  mPasswordEditText.text.toString().trim()
            if (!validateUser(username, password)) return@setOnClickListener

            val result: User? = realm.where(User::class.java).equalTo("username", username).findFirst()
            if (result != null) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
            } else {
                val hash = HashUtils().hashString("SHA-512", password)

                realm.executeTransaction(object : Realm.Transaction {
                    override fun execute(realm: Realm) {
                        realm.insertOrUpdate(User(username, hash))
                    }
                })
                Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMainActivity(userid: String) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        val session = SessionStorage.get(this)
        session.userid = userid
        startActivity(intent)
    }

    private fun validateUser(username: String, password : String) : Boolean {
        if (username.length < 4) {
            Toast.makeText(this, "Username is too short", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 4) {
            Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
