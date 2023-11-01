package com.example.jaderabbittrade.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jaderabbittrade.Constants
import com.example.jaderabbittrade.R

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val fullNameTextView: TextView = view.findViewById(R.id.full_name)
        fullNameTextView.text = getString(R.string.full_name, Constants.firstName + " " + Constants.lastName)

        val currencyUnitName: TextView = view.findViewById(R.id.currency_unit_name)
        currencyUnitName.text = getString(R.string.currency_unit_value, Constants.currency.name)

        val avatarEditButton: ImageButton = view.findViewById(R.id.avatar_edit_button)
        Constants.avatarMap[Constants.avatarID]?.let()
        {
            avatarEditButton.setImageResource(it)
        }
        avatarEditButton.setOnClickListener()
        {
            findNavController().navigate(R.id.action_settingsFragment_to_settingsAvatarFragment)
        }

        val currencyNextButton: ImageButton = view.findViewById(R.id.currency_next_btn)
        currencyNextButton.setOnClickListener()
        {
            findNavController().navigate(R.id.action_settingsFragment_to_settingsCurrencyUnitFragment)
        }
    }
}