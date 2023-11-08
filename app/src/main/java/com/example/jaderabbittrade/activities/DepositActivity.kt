package com.example.jaderabbittrade.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.jaderabbittrade.Constants
import com.example.jaderabbittrade.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import java.util.regex.Pattern

class DepositActivity : AppCompatActivity()
{
    private lateinit var _auth : FirebaseAuth
    private lateinit var _firestore: FirebaseFirestore

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit)

        // Initialize FireBase Auth and Firestore
        _auth = FirebaseAuth.getInstance()
        _firestore = FirebaseFirestore.getInstance()

        // Return to Profile Activity
        val returnButton: ImageButton = findViewById(R.id.return_btn)
        returnButton.setOnClickListener()
        {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        // Initialize views
        val amountEditText: EditText = findViewById(R.id.enter_amount_edit_text)
        val cardNumberEditText: EditText = findViewById(R.id.enter_card_number_edit_text)
        val expiryDateEditText: EditText = findViewById(R.id.enter_expiry_date_edit_text)
        val securityCodeEditText: EditText = findViewById(R.id.enter_security_code_edit_text)
        val cardNameEditText: EditText = findViewById(R.id.enter_card_name_edit_text)
        val saveCardCheckBox: CheckBox = findViewById(R.id.save_card_check_box)
        val confirmButton: Button = findViewById(R.id.deposit_confirm_button)

        amountEditText.hint = getString(R.string.enter_amount_hint, Constants.currency)

        // Get the card from SharedPreferences (if possible)
        val sharedPreferences = getSharedPreferences("CardSharedPrefs", Context.MODE_PRIVATE)
        val cardNumberSPString = sharedPreferences.getString("Card Number", "")
        val expiryDateSPString = sharedPreferences.getString("Expiry Date", "")
        val securityCodeSPString = sharedPreferences.getString("Security Code", "")
        val cardNameSPString = sharedPreferences.getString("Card Name", "")

        if ((cardNumberSPString != "") && (expiryDateSPString != "") && (securityCodeSPString != ""))
        {
            cardNumberEditText.setText(cardNumberSPString)
            expiryDateEditText.setText(expiryDateSPString)
            securityCodeEditText.setText(securityCodeSPString)
            cardNameEditText.setText(cardNameSPString)

            Log.d("Loading Card", "CardNumber=$cardNumberSPString, ExpiryDate=$expiryDateSPString, SecurityCode=$securityCodeSPString, CardName=$cardNameSPString")
        }

        // Amount pattern: A positive double number
        amountEditText.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                try
                {
                    val amount = s.toString().toDouble()
                    if (amount <= 0)
                    {
                        // Amount is not positive, show an error message
                        amountEditText.error = "Amount must be a positive number"
                    }
                    else
                    {
                        // Amount is valid, clear the error message
                        amountEditText.error = null
                    }
                } catch (e: NumberFormatException)
                {
                    // Parsing as a double failed, show an error message
                    amountEditText.error = "Invalid amount format"
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Card number pattern: 16 digits
        cardNumberEditText.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                if (!Pattern.compile("^[0-9]{16}$").matcher(s.toString()).matches())
                {
                    // Card number is not valid, show an error message
                    cardNumberEditText.error = "Invalid card number"
                }
                else
                {
                    // Card number is valid, clear the error message
                    cardNumberEditText.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Date pattern: MM/DD
        expiryDateEditText.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                if (!Pattern.compile("^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$").matcher(s.toString()).matches())
                {
                    // Date is not valid, show an error message
                    expiryDateEditText.error = "Invalid date (MM/DD)"
                }
                else
                {
                    // Date is valid, clear the error message
                    expiryDateEditText.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Security code pattern: 3 digits
        securityCodeEditText.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                if (!Pattern.compile("^[0-9]{3}$").matcher(s.toString()).matches())
                {
                    // Security code is not valid, show an error message
                    securityCodeEditText.error = "Invalid security code"
                }
                else
                {
                    // Security code is valid, clear the error message
                    securityCodeEditText.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Card name pattern: Non-empty string
        cardNameEditText.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                if (s.toString().isBlank())
                {
                    // Card name is empty, show an error message
                    cardNameEditText.error = "Card name is required"
                }
                else
                {
                    // Card name is valid, clear the error message
                    cardNameEditText.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        confirmButton.setOnClickListener()
        {
            if ((amountEditText.error == null) && (cardNumberEditText.error == null) && (expiryDateEditText.error == null) && (securityCodeEditText.error == null) && (cardNameEditText.error == null))
            {
                try
                {
                    var amount = amountEditText.text.toString().toDouble()
                    Toast.makeText(this, "Deposit $amount ${Constants.currency.name}", Toast.LENGTH_SHORT).show()

                    amount /= Constants.exchangeRatesInUSD?.get(Constants.currency)!!

                    // Save SharedPreferences
                    if (saveCardCheckBox.isChecked)
                    {
                        sharedPreferences.edit().apply()
                        {
                            putString("Card Number", cardNumberEditText.text.toString())
                            putString("Expiry Date", expiryDateEditText.text.toString())
                            putString("Security Code", securityCodeEditText.text.toString())
                            putString("Card Name", cardNameEditText.text.toString())

                        }.apply()
                    }

                    Constants.balanceInUSD += amount

                    // Update the "users" collection of the Firestore database
                    val userID: String = _auth.currentUser?.uid ?: ""
                    val userDocumentReference: DocumentReference = _firestore.collection("users").document(userID)
                    userDocumentReference.update("balance", Constants.balanceInUSD)

                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                }
                catch (e: Exception)
                {
                    Log.e("Entering deposit amount", e.toString())
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed()
    {
        super.onBackPressed()

        startActivity(Intent(this, ProfileActivity::class.java))
        finish()
    }
}