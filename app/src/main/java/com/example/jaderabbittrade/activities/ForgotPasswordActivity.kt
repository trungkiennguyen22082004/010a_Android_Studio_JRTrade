package com.example.jaderabbittrade.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jaderabbittrade.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity()
{
    private lateinit var _auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        _auth = FirebaseAuth.getInstance()

        val enterEdt: EditText = findViewById(R.id.enter_edit_text)
        val chooseOptionConfirmBtn: Button = findViewById(R.id.choose_option_confirm_button)

        chooseOptionConfirmBtn.setOnClickListener()
        {
            _auth.sendPasswordResetEmail(enterEdt.text.toString()).addOnCompleteListener()
            {task ->
                if (task.isSuccessful)
                {
                    Log.d(TAG, "Email sent.")
                    Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                }
                else
                {
                    Log.d(TAG, "Sending email failed.")
                    Toast.makeText(this, "Sending email failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Return to Login Activity
        val returnButton: ImageButton = findViewById(R.id.return_btn)
        returnButton.setOnClickListener()
        {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed()
    {
        super.onBackPressed()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}