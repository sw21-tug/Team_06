package com.team06.focuswork

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

open class ThemedAppCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme()
    }

    private fun setTheme() {
        val preferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val backgroundValue =
            (preferences.getString("colorBackground", "light")).toString()
        val accentValue =
            (preferences.getString("colorAccent", "red")).toString()

        setTheme(getBackgroundTheme(backgroundValue))
        setTheme(getAccentTheme(accentValue))
    }

    private fun getBackgroundTheme(preferenceBackgroundThemeValue: String): Int {
        return when (preferenceBackgroundThemeValue) {
            "light" -> R.style.Theme_FocusWork
            "dark" -> R.style.Theme_FocusWork_Dark
            else -> R.style.Theme_FocusWork
        }
    }

    private fun getAccentTheme(preferenceAccentThemeValue: String): Int {
        return when (preferenceAccentThemeValue) {
            "red" -> R.style.Theme_FocusWork_Accent_Red
            "blue" -> R.style.Theme_FocusWork_Accent_Blue
            else -> R.style.Theme_FocusWork_Accent_Red
        }
    }
}