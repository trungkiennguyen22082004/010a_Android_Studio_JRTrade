package com.example.jaderabbittrade.exchange_rates_api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ExchangeRatesAPIUtility
{
    fun getInstance(): Retrofit
    {
        return Retrofit.Builder()
            .baseUrl("http://api.exchangeratesapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}