package com.example.jaderabbittrade.exchange_rates_api

import retrofit2.http.GET

interface IExchangeRatesAPI
{
    @GET("v1/latest?access_key=61c3e2d12e5a1174726f84cd28142d07&symbols=USD,AUD,VND&format=1")
    suspend fun getExchangeRates(): ExchangeRatesRespond
}