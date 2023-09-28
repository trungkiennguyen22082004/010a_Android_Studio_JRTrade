package com.example.jaderabbittrade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout

class ForgotPasswordActivity : AppCompatActivity()
{
    private var _currentLinearLayoutIndex: FPActivityLinearLayout = FPActivityLinearLayout.CHOOSING_OPTION

    private lateinit var _chooseResetOptionSection: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        this.updateActivity()
    }

    private fun updateActivity()
    {
        if (_currentLinearLayoutIndex == FPActivityLinearLayout.CHOOSING_OPTION)
        {

        }
        else if (_currentLinearLayoutIndex == FPActivityLinearLayout.RESET_PASSWORD)
        {

        }
    }
}