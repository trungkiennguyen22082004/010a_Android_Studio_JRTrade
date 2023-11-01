package com.example.jaderabbittrade

import com.example.jaderabbittrade.crypto_api.CryptoCurrency

object Constants
{
    val coinImageMap = mapOf(
        "BTC" to R.drawable.logo_btc,
        "ETH" to R.drawable.logo_eth,
        "USDT" to R.drawable.logo_usdt,
        "BNB" to R.drawable.logo_bnb,
        "XRP" to R.drawable.logo_xrp,
        "USDC" to R.drawable.logo_usdc,
        "SOL" to R.drawable.logo_sol,
        "ADA" to R.drawable.logo_ada,
        "DOGE" to R.drawable.logo_doge,
        "TRX" to R.drawable.logo_trx,
        "TON" to R.drawable.logo_ton,
        "DAI" to R.drawable.logo_dai,
        "MATIC" to R.drawable.logo_matic,
        "WBTC" to R.drawable.logo_wbtc,
        "LTC" to R.drawable.logo_ltc,
        "DOT" to R.drawable.logo_dot,
        "BCH" to R.drawable.logo_bch,
        "LINK" to R.drawable.logo_link,
        "SHIB" to R.drawable.logo_shib,
        "LEO" to R.drawable.logo_leo
    )

    val avatarMap = mapOf(
        0 to R.drawable.ic_profile_white,
        1 to R.drawable.avatar_1,
        2 to R.drawable.avatar_2,
        3 to R.drawable.avatar_3,
        4 to R.drawable.avatar_4,
        5 to R.drawable.avatar_5,
        6 to R.drawable.avatar_6,
        7 to R.drawable.avatar_7,
        8 to R.drawable.avatar_8,
        9 to R.drawable.avatar_9
    )

    enum class Currency
    {
        USD,
        EUR,
        VND,
        AUD
    }

    var currency = Currency.USD

    var exchangeRatesInUSD: HashMap<Currency, Double>? = null

    fun formatPrice(price: Double): String
    {
        return if (exchangeRatesInUSD != null)
        {
            when (currency) {
                Currency.USD -> "$${String.format("%.2f", price * exchangeRatesInUSD!![Currency.USD]!!)}"
                Currency.EUR -> "€${String.format("%.2f", price * exchangeRatesInUSD!![Currency.EUR]!!)}"
                Currency.AUD -> "A$${String.format("%.2f", price * exchangeRatesInUSD!![Currency.AUD]!!)}"
                Currency.VND -> "$${String.format("%.2f", price * exchangeRatesInUSD!![Currency.VND]!!)}₫"
            }
        }
        else
        {
            String.format("%.2f", price)
        }
    }

    var userID: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var avatarID: Int = 1
    var balanceInUSD: Double = 0.00

    var cryptoAssets: List<CryptoCurrency>? = null
}