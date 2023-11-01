package com.example.jaderabbittrade.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jaderabbittrade.Constants
import com.example.jaderabbittrade.R

class SettingsCurrencyUnitFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_currency_unit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val currencyUnitChangeRG: RadioGroup = view.findViewById(R.id.choose_currency_unit_group)
        // Show the current displayed currency unit
        when (Constants.currency)
        {
            Constants.Currency.USD -> view.findViewById<RadioButton>(R.id.usd_btn).isChecked = true
            Constants.Currency.EUR -> view.findViewById<RadioButton>(R.id.eur_btn).isChecked = true
            Constants.Currency.AUD -> view.findViewById<RadioButton>(R.id.aud_btn).isChecked = true
            Constants.Currency.VND -> view.findViewById<RadioButton>(R.id.vnd_btn).isChecked = true
        }

        val saveButton: Button = view.findViewById(R.id.save_button)
        saveButton.setOnClickListener()
        {
            val result = when (currencyUnitChangeRG.checkedRadioButtonId)
            {
                R.id.usd_btn -> Constants.Currency.USD
                R.id.eur_btn -> Constants.Currency.EUR
                R.id.aud_btn -> Constants.Currency.AUD
                R.id.vnd_btn -> Constants.Currency.VND
                else -> Constants.Currency.USD
            }

            Constants.currency = result

            findNavController().navigate(R.id.action_settingsCurrencyUnitFragment_to_settingsFragment)
        }
    }
}