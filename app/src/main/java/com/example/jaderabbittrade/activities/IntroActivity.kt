package com.example.jaderabbittrade.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.jaderabbittrade.R

class IntroActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val nextButton: ImageButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener()
        {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }
}