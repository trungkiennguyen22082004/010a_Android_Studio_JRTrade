package com.example.jaderabbittrade.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jaderabbittrade.Constants
import com.example.jaderabbittrade.R
import com.example.jaderabbittrade.wallets.CryptoBalance
import com.example.jaderabbittrade.wallets.CryptosWalletAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WalletActivity : AppCompatActivity()
{
    private lateinit var _auth : FirebaseAuth
    private lateinit var _firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        // Initialize FireBase Auth and Firestore
        _auth = FirebaseAuth.getInstance()
        _firestore = FirebaseFirestore.getInstance()

        // Initialize the floating bar
        val floatingBar: CoordinatorLayout = findViewById(R.id.floating_bar)
        val fBHomeButton: FloatingActionButton = floatingBar.findViewById(R.id.floating_action_home_button)
        fBHomeButton.setOnClickListener()
        {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }

        val fBNewsButton: ImageButton = floatingBar.findViewById(R.id.news_button)
        fBNewsButton.setOnClickListener()
        {
            startActivity(Intent(applicationContext, NewsActivity::class.java))
            finish()
        }

        val fBProfileButton: ImageButton = floatingBar.findViewById(R.id.profile_button)
        fBProfileButton.setOnClickListener()
        {
            startActivity(Intent(applicationContext, ProfileActivity::class.java))
            finish()
        }

        val fBWalletButton: ImageButton = floatingBar.findViewById(R.id.wallet_button)
        fBWalletButton.isClickable = false
        fBWalletButton.setImageResource(R.drawable.ic_wallet_purple_pink)
        val fBWalletText: TextView = floatingBar.findViewById(R.id.wallet_button_text)
        fBWalletText.setTextColor(ContextCompat.getColor(applicationContext, R.color.purple_pink))

        val fBSettingsButton: ImageButton = floatingBar.findViewById(R.id.settings_button)
        fBSettingsButton.setOnClickListener()
        {
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
            finish()
        }

        val userCryptoWalletTextView: TextView = findViewById(R.id.crypto_wallet_title)
        userCryptoWalletTextView.text = getString(R.string.user_crypto_wallets, Constants.firstName)

        val cryptosWalletRecyclerView: RecyclerView = findViewById(R.id.wallet_cryptos)

        try
        {
            val userID: String = _auth.currentUser?.uid ?: ""
            val cryptosWalletDocumentReference = _firestore.collection("usersCryptoBalance").document(userID)
            cryptosWalletDocumentReference.get().addOnSuccessListener()
            { documentSnapshot ->
                if (documentSnapshot.exists())
                {
                    Log.d("Retrieving Cryptos Wallet", documentSnapshot.data.toString())

                    val dataMap = documentSnapshot.data as Map<String, Any>

                    val cryptoBalanceList = mutableListOf<CryptoBalance>()

                    for ((coinCode, amount) in dataMap)
                    {
                        val cryptoBalance = CryptoBalance(coinCode, amount as Double)
                        cryptoBalanceList.add(cryptoBalance)
                    }

                    cryptosWalletRecyclerView.layoutManager = LinearLayoutManager(this)
                    val cryptosWalletAdapter = CryptosWalletAdapter(applicationContext, cryptoBalanceList)
                    cryptosWalletAdapter.onItemClickListener = {
                        val intent = Intent(this, TradingActivity::class.java)
                        intent.putExtra("Trading type", "Sell")
                        intent.putExtra("Coin code", it.coinCode)
                        intent.putExtra("Coin amount", it.amount)

                        startActivity(intent)
                        finish()
                    }
                    cryptosWalletRecyclerView.adapter = cryptosWalletAdapter
                }
            }
        }
        catch (e: Exception)
        {
            Log.e("Firestore connecting error", e.toString())
        }
    }
}