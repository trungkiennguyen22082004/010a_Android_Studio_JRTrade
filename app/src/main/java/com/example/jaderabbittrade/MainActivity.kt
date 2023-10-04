package com.example.jaderabbittrade

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity()
{
    private lateinit var _auth : FirebaseAuth
    private var _user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }
}