package com.example.jaderabbittrade.exchange_rates_api

data class ExchangeRatesRespond(
    val base: String,
    val date: String,
    val rates: Rates,
    val success: Boolean,
    val timestamp: Int
)