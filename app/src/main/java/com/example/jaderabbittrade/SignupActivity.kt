package com.example.jaderabbittrade

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity()
{
    private lateinit var _auth : FirebaseAuth
    private lateinit var _firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize views
        val fNameEdt: EditText = findViewById(R.id.first_name_edit_text)
        val lNameEdt: EditText = findViewById(R.id.given_name_edit_text)
        val emailEdt: EditText = findViewById(R.id.email_edit_text)
        val pwdEdt: EditText = findViewById(R.id.password_edit_text)
        val signUpBtn: Button = findViewById(R.id.signup_button)
        val loginRecommendation: TextView = findViewById(R.id.login_recommendation_text_view)
        val prgBar: ProgressBar = findViewById(R.id.progress_bar)

        // Initialize FireBase Auth and Firestore
        _auth = FirebaseAuth.getInstance()
        _firestore = FirebaseFirestore.getInstance()

        signUpBtn.setOnClickListener()
        {
            prgBar.progress = View.VISIBLE

            val fName: String = fNameEdt.text.toString()
            val lName: String = lNameEdt.text.toString()
            val email: String = emailEdt.text.toString()
            val pwd: String = pwdEdt.text.toString()

            if (TextUtils.isEmpty(fName))
            {
                Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(lName))
            {
                Toast.makeText(this, "Please enter your given name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(email))
            {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pwd))
            {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pwd.length < 6)
            {
                Toast.makeText(this, "Password length must be greater than 5", Toast.LENGTH_SHORT).show()
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

                    // Store the user data
                    val userID: String = _auth.currentUser?.uid ?: ""
                    val documentReference: DocumentReference = _firestore.collection("users").document(userID)
                    val user = HashMap<String, Any>()
                    user["firstName"] = fName
                    user["lastName"] = lName
                    user["email"] = email
                    documentReference.set(user).addOnSuccessListener()
                    {
                        Log.d(TAG, "onSuccess:user profile is created for $userID")
                        Toast.makeText(this, "User Profile is created for $userID", Toast.LENGTH_SHORT).show()
                    }

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