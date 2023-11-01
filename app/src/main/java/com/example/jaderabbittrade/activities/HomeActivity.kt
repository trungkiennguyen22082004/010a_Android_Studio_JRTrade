package com.example.jaderabbittrade.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jaderabbittrade.Constants
import com.example.jaderabbittrade.R
import com.example.jaderabbittrade.crypto_api.CryptoAPIUtility
import com.example.jaderabbittrade.crypto_api.CryptoAdapter
import com.example.jaderabbittrade.crypto_api.CryptoCurrency
import com.example.jaderabbittrade.crypto_api.ICryptoAPI
import com.example.jaderabbittrade.exchange_rates_api.ExchangeRatesAPIUtility
import com.example.jaderabbittrade.exchange_rates_api.IExchangeRatesAPI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity()
{
    private lateinit var _cryptoRecyclerView: RecyclerView
    private lateinit var _cryptoAdapter: CryptoAdapter
    private var _topCryptoAssets: List<CryptoCurrency> = listOf()

    private lateinit var _hotButton: Button
    private lateinit var _priceButton: Button
    private lateinit var _change24hButton: Button

    val sortType: String
        get() = when
        {
            _hotButton.backgroundTintList == null -> "Hot"
            _priceButton.backgroundTintList == null -> "Price"
            _change24hButton.backgroundTintList == null -> "24h Change"
            else -> "Unknown"
        }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize the toolbar
        val toolBar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        // supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.title = "Home"

        _cryptoRecyclerView = findViewById(R.id.crypto_currency_recycle_view)
        _cryptoRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the floating bar
        val floatingBar: CoordinatorLayout = findViewById(R.id.floating_bar)
        val fBHomeButton: FloatingActionButton = floatingBar.findViewById(R.id.floating_action_home_button)
        fBHomeButton.isClickable = false
        fBHomeButton.foreground = ContextCompat.getDrawable(applicationContext, R.drawable.floating_home_background_2)

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

        this.getExchangeRatesData()

        this.getTopCurrencyList()

        // Sort functionality
        _hotButton = findViewById(R.id.hot_button)
        _priceButton = findViewById(R.id.price_button)
        _change24hButton = findViewById(R.id._24h_change_button)
        _hotButton.setOnClickListener()
        {
            _hotButton.backgroundTintList = null
            _priceButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.white_white_gray))
            _change24hButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.white_white_gray))

            _cryptoAdapter.sort(this.sortType)
        }
        _priceButton.setOnClickListener()
        {
            _priceButton.backgroundTintList = null
            _hotButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.white_white_gray))
            _change24hButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.white_white_gray))

            _cryptoAdapter.sort(this.sortType)
        }
        _change24hButton.setOnClickListener()
        {
            _change24hButton.backgroundTintList = null
            _hotButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.white_white_gray))
            _priceButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.white_white_gray))

            _cryptoAdapter.sort(this.sortType)
        }
    }

    private fun getTopCurrencyList()
    {
        if (Constants.cryptoAssets == null)
        {
            lifecycleScope.launch()
            {
                val response = try
                {
                    CryptoAPIUtility.getInstance().create(ICryptoAPI::class.java).getCryptoAssets()
                }
                catch (e: Exception)
                {
                    Log.e("API Response", e.toString())

                    null
                }

                response?.let()
                { cryptoResponse ->
                    // All Crypto Assets from the API
                    val cryptoAssets = cryptoResponse.data.cryptoCurrencyList

                    Constants.cryptoAssets = cryptoAssets

                    setUpTopCryptosAndRecyclerView(cryptoAssets)
                }
            }
        }
        else
        {
            this.setUpTopCryptosAndRecyclerView(Constants.cryptoAssets!!)
        }
    }

    private fun setUpTopCryptosAndRecyclerView(cryptoAssets: List<CryptoCurrency>)
    {
        // Get the 20 Top Crypto Assets from the above list
        _topCryptoAssets = cryptoAssets.take(20)

        for (cryptoAsset in _topCryptoAssets)
        {
            Log.d("Retrieving Crypto Asset", cryptoAsset.toString())
        }

        _cryptoAdapter = createAdapter()
        _cryptoRecyclerView.adapter = _cryptoAdapter
    }

    private fun getExchangeRatesData()
    {
        if (Constants.exchangeRatesInUSD == null)
        {
            lifecycleScope.launch()
            {
                val response = try
                {
                    ExchangeRatesAPIUtility.getInstance().create(IExchangeRatesAPI::class.java).getExchangeRates()
                }
                catch (e: Exception)
                {
                    Log.e("API Response", e.toString())

                    null
                }

                response?.let()
                { exchangeRatesRespond ->
                    // The data from the API is based on EUR
                    val ratesInEUR = exchangeRatesRespond.rates

                    // Convert so that the rates are based on USD
                    val ratesInUSD = HashMap<Constants.Currency, Double>()
                    ratesInUSD[Constants.Currency.USD] = 1.0
                    ratesInUSD[Constants.Currency.EUR] = 1.0 / ratesInEUR.USD
                    ratesInUSD[Constants.Currency.AUD] = ratesInEUR.AUD / ratesInEUR.USD
                    ratesInUSD[Constants.Currency.VND] = ratesInEUR.VND / ratesInEUR.USD

                    Log.d("ExchangeRatesAPIRespond", ratesInUSD.toString())

                    Constants.exchangeRatesInUSD = ratesInUSD
                }
            }
        }
    }

    // Handle the menu and filter function
    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.home_menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)

        if (searchItem != null)
        {
            val searchView = searchItem.actionView as SearchView
            searchView.queryHint = "Type here to Search"

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
            {
                override fun onQueryTextSubmit(query: String?): Boolean
                {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean
                {
                    _cryptoAdapter.search(newText ?: "", sortType)
                    return true
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun createAdapter(): CryptoAdapter
    {
        val cryptoAdapter = CryptoAdapter(applicationContext, _topCryptoAssets.toMutableList())
        cryptoAdapter.onItemClickListener = {
            val intent = Intent(this, TradingActivity::class.java)
            intent.putExtra("Trading type", "Buy")
            intent.putExtra("Coin name", it.name)
            intent.putExtra("Coin code", it.symbol)
            intent.putExtra("Coin price", it.quotes.firstOrNull()?.price ?: 0.0)

            startActivity(intent)
            finish()
        }

        return cryptoAdapter
    }
}