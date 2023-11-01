package com.example.jaderabbittrade.crypto_api

import retrofit2.http.GET

interface ICryptoAPI
{
    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=500")
    suspend fun getCryptoAssets(): CryptoResponse
}