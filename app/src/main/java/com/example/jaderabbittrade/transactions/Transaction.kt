package com.example.jaderabbittrade.transactions

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Transaction(
    val coinName: String,
    val dateTime: Date,
    val tradingType: TradingType,
    val amountInDollars: Float,
    val status: TransactionStatus
)
{
    fun splitDateTime(): Map<String, String> {
        // Create a SimpleDateFormat instance for date and time formatting
        val dateFormat = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss z", Locale.getDefault())

        // Format the date and time components as strings
        val dateString = dateFormat.format(this.dateTime)
        val timeString = timeFormat.format(this.dateTime)

        // Return a Map of the date and time components
        return mapOf(
            "Date" to dateString,
            "Time" to timeString
        )
    }
}
