package com.example.jaderabbittrade

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity()
{
    private lateinit var _auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize views
        val emailEdt: EditText = findViewById(R.id.email_edit_text)
        val pwdEdt: EditText = findViewById(R.id.password_edit_text)
        val signUpBtn: Button = findViewById(R.id.signup_button)
        val loginRecommendation: TextView = findViewById(R.id.login_recommendation_text_view)
        val prgBar: ProgressBar = findViewById(R.id.progress_bar)

        // Initialize FireBase Auth
        _auth = FirebaseAuth.getInstance()

        signUpBtn.setOnClickListener()
        {
            prgBar.progress = View.VISIBLE

            val email: String = emailEdt.text.toString()
            val pwd: String = pwdEdt.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            _auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(this)
            { task ->
                prgBar.visibility = View.GONE

                if (task.isSuccessful)
                {
                    // If sign up successfully
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(this, "Register successful.", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                }
                else
                {
                    // If sign up failed
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginRecommendation.setOnClickListener()
        {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    public override fun onStart()
    {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = _auth.currentUser
        if (currentUser != null)
        {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }
}