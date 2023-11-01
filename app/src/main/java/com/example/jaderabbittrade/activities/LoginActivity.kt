package com.example.jaderabbittrade.activities

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
import com.example.jaderabbittrade.Constants
import com.example.jaderabbittrade.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity()
{
    private lateinit var _auth : FirebaseAuth
    private lateinit var _firestore: FirebaseFirestore

    private lateinit var _prgBar: ProgressBar
    private lateinit var _loginBtn: Button
    private lateinit var _signupRecommendation: TextView
    private lateinit var _forgotPwd: TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        val emailEdt: EditText = findViewById(R.id.email_edit_text)
        val pwdEdt: EditText = findViewById(R.id.password_edit_text)

        _loginBtn = findViewById(R.id.login_button)
        _signupRecommendation = findViewById(R.id.signup_recommendation_text_view)
        _forgotPwd = findViewById(R.id.forgot_text_view)
        _prgBar = findViewById(R.id.progress_bar)

        // Initialize FireBase Auth and FireStore
        _auth = FirebaseAuth.getInstance()
        _firestore = FirebaseFirestore.getInstance()

        // Set click listeners
        _loginBtn.setOnClickListener()
        {
            this.enableProgressBar()

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
                _prgBar.visibility = View.GONE

                // If login successfully
                if (task.isSuccessful)
                {
                    Log.d(TAG, "loginWithEmail:success")
                    Toast.makeText(this, "Login successfully.", Toast.LENGTH_SHORT).show()

                    this.startSession()
                }
                // If login failed
                else
                {
                    Log.w(TAG, "loginWithEmail:failure", task.exception)
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        _forgotPwd.setOnClickListener()
        {
            startActivity(Intent(applicationContext, ForgotPasswordActivity::class.java))
            finish()
        }

        _signupRecommendation.setOnClickListener()
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
            this.enableProgressBar()

            this.startSession()
        }
    }

    private fun startSession()
    {
        val userID: String = _auth.currentUser?.uid ?: ""

        val userDocumentReference: DocumentReference = _firestore.collection("users").document(userID)
        userDocumentReference.get().addOnSuccessListener()
        { document ->
            Constants.userID = userID
            Constants.firstName = document.getString("firstName") ?: ""
            Constants.lastName = document.getString("lastName") ?: ""
            Constants.email = document.getString("email") ?: ""
            Constants.avatarID = (document.getLong("avatarID") ?: 0).toInt()
            Constants.balanceInUSD = (document.getDouble("balance") ?: 0).toDouble()

            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }
    }

    private fun enableProgressBar()
    {
        _prgBar.visibility = View.VISIBLE
        _prgBar.progress = View.VISIBLE
        _loginBtn.isClickable = false
        _signupRecommendation.isClickable = false
        _forgotPwd.isClickable = false
    }
}