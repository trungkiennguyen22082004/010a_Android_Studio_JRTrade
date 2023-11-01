package com.example.jaderabbittrade.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jaderabbittrade.Constants
import com.example.jaderabbittrade.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class TradingActivity : AppCompatActivity()
{
    private lateinit var _auth : FirebaseAuth
    private lateinit var _firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trading)

        // Initialize FireBase Auth and Firestore
        _auth = FirebaseAuth.getInstance()
        _firestore = FirebaseFirestore.getInstance()

        // Initialize the views
        val tradingTitleTextView: TextView = findViewById(R.id.trading_title)
        val amountTextView: TextView = findViewById(R.id.amount_text_view)
        val payGetButton: Button = findViewById(R.id.submitting_button)
        val amountEditText: EditText = findViewById(R.id.amount_edit_text)
        val amountWarningTextView: TextView = findViewById(R.id.amount_warning_text_view)

        amountWarningTextView.visibility = View.INVISIBLE

        val bundle: Bundle? = intent.extras
        val tradingType = bundle?.getString("Trading type") ?: ""

        if (tradingType == "Buy")
        {
            val coinName: String = bundle?.getString("Coin name") ?: ""
            val coinCode: String = bundle?.getString("Coin code") ?: ""
            val coinPrice: Double = bundle?.getDouble("Coin price") ?: 0.0

            tradingTitleTextView.text = getString(R.string.buying_capital, coinName)

            amountTextView.text = getString(R.string.amount, coinCode)

            amountEditText.addTextChangedListener(object : TextWatcher
            {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
                {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
                {
                }

                override fun afterTextChanged(s: Editable?)
                {
                    if (s.toString() != "")
                    {
                        payGetButton.text = getString(R.string.pay_amount, s.toString().toDouble() * coinPrice)
                    }
                    else
                    {
                        payGetButton.text = getString(R.string.pay_amount, 0.0)
                    }
                }
            })

            payGetButton.setOnClickListener()
            {
                if (Constants.balanceInUSD >= amountEditText.text.toString().toDouble() * coinPrice)
                {
                    val userID: String = _auth.currentUser?.uid ?: ""

                    // Update user balance
                    Constants.balanceInUSD -= amountEditText.text.toString().toDouble() * coinPrice
                    val userDocumentReference: DocumentReference = _firestore.collection("users").document(userID)
                    userDocumentReference.update("balance", Constants.balanceInUSD)

                    // Update the user wallet
                    val userCryptoBalanceDocumentReference: DocumentReference = _firestore.collection("usersCryptoBalance").document(userID)
                    userCryptoBalanceDocumentReference.get().addOnSuccessListener()
                    {
                        var currentAmount: Double = it.getDouble(coinCode) ?: 0.0
                        currentAmount += amountEditText.text.toString().toDouble()

                        userCryptoBalanceDocumentReference.update(coinCode, currentAmount)
                    }

                    // Add a new transaction document
                    val transactionCollectionReference: CollectionReference = _firestore.collection("transactions")
                    val transaction = HashMap<String, Any>()
                    transaction["amountInDollars"] = amountEditText.text.toString().toDouble() * coinPrice
                    transaction["coinName"] = coinCode
                    transaction["dateTime"] = Timestamp.now()
                    transaction["status"] = "SUCCESS"
                    transaction["tradingType"] = "BOUGHT"
                    transaction["userID"] = userID
                    transactionCollectionReference.add(transaction).addOnSuccessListener()
                    {
                        Log.d(ContentValues.TAG, "onSuccess:transaction is created for $userID")
                        Toast.makeText(this, "Buying successfully", Toast.LENGTH_SHORT).show()
                    }

                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    finish()
                }
                else
                {
                    amountWarningTextView.text = getString(R.string.insufficient_balance)
                    amountWarningTextView.visibility = View.VISIBLE
                }
            }
        }
        else
        {
            val coinCode: String = bundle?.getString("Coin code") ?: ""
            val totalCoinAmount: Double = bundle?.getDouble("Coin amount") ?: 0.0

            var coinName = ""
            var coinPrice = 0.0

            if (Constants.cryptoAssets != null)
            {
                for (cryptoAsset in Constants.cryptoAssets!!)
                {
                    if (cryptoAsset.symbol == coinCode)
                    {
                        coinName = cryptoAsset.name
                        coinPrice = cryptoAsset.quotes[0].price
                    }
                }
            }

            tradingTitleTextView.text = getString(R.string.selling_capital, coinName)

            amountTextView.text = getString(R.string.amount, coinCode)

            payGetButton.text = getString(R.string.get)

            amountEditText.addTextChangedListener(object : TextWatcher
            {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
                {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
                {
                }

                override fun afterTextChanged(s: Editable?)
                {
                    if (s.toString() != "")
                    {
                        payGetButton.text = getString(R.string.get_amount, (s.toString()).toDouble() * coinPrice)
                    }
                    else
                    {
                        payGetButton.text = getString(R.string.get_amount, 0.0)
                    }
                }
            })

            payGetButton.setOnClickListener()
            {
                if (totalCoinAmount >= amountEditText.text.toString().toDouble())
                {
                    val userID: String = _auth.currentUser?.uid ?: ""

                    // Update user balance
                    Constants.balanceInUSD += amountEditText.text.toString().toDouble() * coinPrice
                    val userDocumentReference: DocumentReference = _firestore.collection("users").document(userID)
                    userDocumentReference.update("balance", Constants.balanceInUSD)

                    // Update the user wallet
                    val userCryptoBalanceDocumentReference: DocumentReference = _firestore.collection("usersCryptoBalance").document(userID)
                    userCryptoBalanceDocumentReference.get().addOnSuccessListener()
                    {
                        var currentAmount: Double = it.getDouble(coinCode) ?: 0.0
                        currentAmount -= amountEditText.text.toString().toDouble()

                        userCryptoBalanceDocumentReference.update(coinCode, currentAmount)
                    }

                    // Add a new transaction document
                    val transactionCollectionReference: CollectionReference = _firestore.collection("transactions")
                    val transaction = HashMap<String, Any>()
                    transaction["amountInDollars"] = amountEditText.text.toString().toDouble() * coinPrice
                    transaction["coinName"] = coinCode
                    transaction["dateTime"] = Timestamp.now()
                    transaction["status"] = "SUCCESS"
                    transaction["tradingType"] = "SOLD"
                    transaction["userID"] = userID
                    transactionCollectionReference.add(transaction).addOnSuccessListener()
                    {
                        Log.d(ContentValues.TAG, "onSuccess:transaction is created for $userID")
                        Toast.makeText(this, "Buying successfully", Toast.LENGTH_SHORT).show()
                    }

                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    finish()
                }
                else
                {
                    amountWarningTextView.text = getString(R.string.insufficient_balance)
                    amountWarningTextView.visibility = View.VISIBLE
                }
            }
        }
    }
}