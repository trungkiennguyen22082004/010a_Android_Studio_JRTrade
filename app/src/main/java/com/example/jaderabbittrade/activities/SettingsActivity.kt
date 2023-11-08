package com.example.jaderabbittrade.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jaderabbittrade.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SettingsActivity : AppCompatActivity()
{
    private lateinit var _auth : FirebaseAuth
    private var _user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        _auth = FirebaseAuth.getInstance()
        _user = _auth.currentUser
        if (_user == null)
        {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        val logoutBtn: Button = findViewById(R.id.logout_button)
        logoutBtn.setOnClickListener()
        {
            _auth.signOut()
            Log.d(ContentValues.TAG, "logoutWithEmail:success")
            Toast.makeText(this, "Logout successfully.", Toast.LENGTH_SHORT).show()

            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        val homeButton: ImageButton = findViewById(R.id.home_button)
        homeButton.setOnClickListener()
        {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed()
    {
        super.onBackPressed()

        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

}