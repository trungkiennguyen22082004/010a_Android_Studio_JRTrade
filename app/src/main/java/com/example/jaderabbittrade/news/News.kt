package com.example.jaderabbittrade.news

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class News(
    val author: String,
    val dateTime: Date,
    val content: String,
    val title: String) {

    val dateTimeInString: String
        get()
        {
            val dateTimeFormat = SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.getDefault())
            return dateTimeFormat.format(dateTime)
        }
}