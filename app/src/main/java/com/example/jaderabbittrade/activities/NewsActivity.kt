package com.example.jaderabbittrade.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jaderabbittrade.R
import com.example.jaderabbittrade.news.News
import com.example.jaderabbittrade.news.NewsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class NewsActivity : AppCompatActivity()
{
    private lateinit var _auth : FirebaseAuth
    private lateinit var _realtimeDB: FirebaseDatabase

    private val _news: MutableList<News> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        // Initialize FireBase Auth and Firestore
        _auth = FirebaseAuth.getInstance()
        _realtimeDB = FirebaseDatabase.getInstance()

        val context = this

        // Initialize the floating bar
        val floatingBar: CoordinatorLayout = findViewById(R.id.floating_bar)
        val fBHomeButton: FloatingActionButton = floatingBar.findViewById(R.id.floating_action_home_button)
        fBHomeButton.setOnClickListener()
        {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }

        // Initialize the navbar functionality
        val fBNewsButton: ImageButton = floatingBar.findViewById(R.id.news_button)
        fBNewsButton.isClickable = false
        fBNewsButton.setImageResource(R.drawable.ic_news_purple_pink)
        val fBNewsText: TextView = floatingBar.findViewById(R.id.news_button_text)
        fBNewsText.setTextColor(ContextCompat.getColor(applicationContext, R.color.purple_pink))

        val fBProfileButton: ImageButton = floatingBar.findViewById(R.id.profile_button)
        fBProfileButton.setOnClickListener()
        {
            startActivity(Intent(applicationContext, ProfileActivity::class.java))
            finish()
        }

        val fBWalletButton: ImageButton = floatingBar.findViewById(R.id.wallet_button)
        fBWalletButton.setOnClickListener()
        {
            startActivity(Intent(applicationContext, WalletActivity::class.java))
            finish()
        }

        val fBSettingsButton: ImageButton = floatingBar.findViewById(R.id.settings_button)
        fBSettingsButton.setOnClickListener()
        {
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
            finish()
        }

        // Get the news data from Realtime Database
        val newsReference = _realtimeDB.getReference("cryptocurrencyNews")

        val postListener = object : ValueEventListener
        {
            override fun onDataChange(parentSnapshot: DataSnapshot)
            {
                for (newsSnapshot in parentSnapshot.children)
                {
                    val author = newsSnapshot.child("author").getValue(String::class.java)
                    val content = newsSnapshot.child("content").getValue(String::class.java)
                    val dateTimeString = newsSnapshot.child("dateTime").getValue(String::class.java)
                    val title = newsSnapshot.child("title").getValue(String::class.java)

                    if (author != null && content != null && dateTimeString != null && title != null)
                    {
                        Log.d("Accessing Realtime DB", "author=$author, content=$content, dataTime=$dateTimeString, title=$title")

                        // Parse the dateTimeString into a Date object
                        val dateTimeFormat = SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss zzz",Locale.getDefault())
                        try
                        {
                            val dateTime = dateTimeFormat.parse(dateTimeString)

                            _news.add(News(author = author, content = content, dateTime = dateTime, title = title))
                        }
                        catch (e: Exception)
                        {
                            Log.e("Converting news' date-time", e.toString())
                        }
                    }
                    else
                    {
                        Log.e("Accessing Realtime DB", "Some data may be null:\nauthor=$author, content=$content, dataTime=$dateTimeString, title=$title")
                    }
                }

                Log.d("Accessing Realtime DB Outputs", _news.toString())

                _news.sortByDescending { it.dateTime }

                val newsRecyclerView: RecyclerView = findViewById(R.id.news_recycle_view)
                newsRecyclerView.layoutManager = LinearLayoutManager(context)
                val newsAdapter = NewsAdapter(applicationContext, _news)
                newsAdapter.onItemLongClickListener = {
                    startActivity(detailedNewsActivityIntent(it.author, it.dateTimeInString, it.content, it.title))
                    finish()
                }
                newsRecyclerView.adapter = newsAdapter
            }

            override fun onCancelled(databaseError: DatabaseError)
            {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        newsReference.addValueEventListener(postListener)
    }

    private fun detailedNewsActivityIntent(author: String, dateTime: String, content: String, title: String): Intent
    {
        val intent = Intent(this, DetailedNewsActivity::class.java)
        intent.putExtra("Author", author)
        intent.putExtra("Date Time", dateTime)
        intent.putExtra("Content", content)
        intent.putExtra("Title", title)

        return intent
    }
}