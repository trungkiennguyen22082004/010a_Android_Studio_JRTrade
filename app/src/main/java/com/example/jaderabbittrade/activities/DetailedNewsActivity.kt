package com.example.jaderabbittrade.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.jaderabbittrade.R

class DetailedNewsActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_news)

        // Return to News Activity
        val returnButton: ImageButton = findViewById(R.id.return_btn)
        returnButton.setOnClickListener()
        {
            startActivity(Intent(this, NewsActivity::class.java))
            finish()
        }

        val bundle: Bundle? = intent.extras
        val author = bundle?.getString("Author") ?: ""
        val dateTime = bundle?.getString("Date Time") ?: ""
        var content = bundle?.getString("Content") ?: ""
        val title = bundle?.getString("Title") ?: ""

        val authorTextView: TextView = findViewById(R.id.news_author)
        authorTextView.text = author
        val dateTimeTextView: TextView = findViewById(R.id.news_datetime)
        dateTimeTextView.text = dateTime
        val titleTextView: TextView = findViewById(R.id.news_title)
        titleTextView.text = title
        val contentTextView: TextView = findViewById(R.id.news_content)
        content = content.replace("<NewLine>", "\n")

        contentTextView.text = getString(R.string.news_content, content)
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed()
    {
        super.onBackPressed()

        startActivity(Intent(this, NewsActivity::class.java))
        finish()
    }
}