package com.example.jaderabbittrade.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jaderabbittrade.Constants
import com.example.jaderabbittrade.R
import com.example.jaderabbittrade.transactions.TradingType
import com.example.jaderabbittrade.transactions.Transaction
import com.example.jaderabbittrade.transactions.TransactionAdapter
import com.example.jaderabbittrade.transactions.TransactionStatus
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class ProfileActivity : AppCompatActivity()
{
    private lateinit var _auth : FirebaseAuth
    private lateinit var _firestore: FirebaseFirestore

    private var _transactions: MutableList<Transaction> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize FireBase Auth and Firestore
        _auth = FirebaseAuth.getInstance()
        _firestore = FirebaseFirestore.getInstance()

        // Initialize the views
        val avatarImageView: ImageView = findViewById(R.id.avatar)
        val helloUserTextView: TextView = findViewById(R.id.hello_user_text_view)
        val currencyUnitTextView: TextView = findViewById(R.id.currency_unit)
        val balanceValueTextView: TextView = findViewById(R.id.balance_value)
        val transactionsRecyclerView: RecyclerView = findViewById(R.id.transactions)

        // Initialize the floating bar
        val floatingBar: CoordinatorLayout = findViewById(R.id.floating_bar)
        val fBHomeButton: FloatingActionButton = floatingBar.findViewById(R.id.floating_action_home_button)
        fBHomeButton.setOnClickListener()
        {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }

        val fBProfileButton: ImageButton = floatingBar.findViewById(R.id.profile_button)
        fBProfileButton.isClickable = false
        fBProfileButton.setImageResource(R.drawable.ic_profile_purple_pink)
        val fBProfileText: TextView = floatingBar.findViewById(R.id.profile_button_text)
        fBProfileText.setTextColor(ContextCompat.getColor(applicationContext, R.color.purple_pink))

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

        Constants.avatarMap[Constants.avatarID]?.let()
        {
            avatarImageView.setImageResource(it)
        }
        helloUserTextView.text = getString(R.string.hello_user, Constants.firstName)
        currencyUnitTextView.text = getString(R.string.balance_in_currency_unit, Constants.currency.name)
        balanceValueTextView.text = getString(R.string.balance_value, Constants.formatPrice(Constants.balanceInUSD))

        try
        {
            val transactionsCollectionReference: CollectionReference = _firestore.collection("transactions")
            transactionsCollectionReference.get().addOnSuccessListener()
            {documents ->
                for (document in documents)
                {
                    if (document.getString("userID") == Constants.userID)
                    {
                        _transactions.add(
                            Transaction(coinName = document.getString("coinName") ?: "",
                                                     dateTime = document.getTimestamp("dateTime")?.toDate() ?: Date(),
                                                     tradingType = TradingType.valueOf(document.getString("tradingType") ?: ""),
                                                     amountInDollars = (document.getDouble("amountInDollars") ?: 0.00).toFloat(),
                                                     status = TransactionStatus.valueOf(document.getString("status") ?: ""))
                        )

                        Log.d("Document", "Loaded document: ${_transactions[_transactions.size - 1]}")
                    }

                    // Sort the transactions by dateTime in descending order
                    _transactions.sortByDescending { it.dateTime }

                    // Set up RecyclerView adapter after sorting
                    transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
                    transactionsRecyclerView.adapter = TransactionAdapter(applicationContext, _transactions)
                }
            }
        }
        catch (e: Exception)
        {
            Log.e("Firestore connecting error", e.toString())
        }
    }
}