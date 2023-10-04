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

class LoginActivity : AppCompatActivity()
{
    private lateinit var _auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        val emailEdt: EditText = findViewById(R.id.email_edit_text)
        val pwdEdt: EditText = findViewById(R.id.password_edit_text)
        val loginBtn: Button = findViewById(R.id.login_button)
        val signupRecommendation: TextView = findViewById(R.id.signup_recommendation_text_view)
        val forgotPwd: TextView = findViewById(R.id.forgot_text_view)
        val prgBar: ProgressBar = findViewById(R.id.progress_bar)

        // Initialize FireBase Auth
        _auth = FirebaseAuth.getInstance()

        // Set click listeners
        loginBtn.setOnClickListener()
        {
            prgBar.progress = View.VISIBLE

            val email: String = emailEdt.text.toString()
            val password: String = pwdEdt.text.toString()

            if (TextUtils.isEmpty(email))
            {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            _auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this)
            {task ->
                prgBar.visibility = View.GONE

                // If login successfully
                if (task.isSuccessful)
                {
                    Log.d(TAG, "loginWithEmail:success")
                    Toast.makeText(this, "Login successfully.", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
                // If login failed
                else
                {
                    Log.w(TAG, "loginWithEmail:failure", task.exception)
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        forgotPwd.setOnClickListener()
        {
            startActivity(Intent(applicationContext, ForgotPasswordActivity::class.java))
            finish()
        }

        signupRecommendation.setOnClickListener()
        {
            startActivity(Intent(applicationContext, SignupActivity::class.java))
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