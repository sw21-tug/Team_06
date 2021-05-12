package com.team06.focuswork

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import java.util.*

open class ThemedAppCompatActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme()

        //Load default values for settings in case user hasn't selected values yet
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)

        checkLocale()

        // set listener for settings
        val preferences :SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        //Unregister settings listener
        val preferences :SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.unregisterOnSharedPreferenceChangeListener(this)

        super.onDestroy()
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

    /**
     * If the preferred language is not the current language,
     * restarts the activity with the preferred language
     */
    @Suppress("DEPRECATION")
    private fun checkLocale() {
        val preferences :SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val languageKey = (preferences.getString("language", "en")).toString()
        if(languageKey.toLowerCase() != resources.configuration.locale.language.toLowerCase())
            onChangedLanguage(languageKey)
    }

    @Suppress("DEPRECATION")
    private fun onChangedLanguage(languageKey: String){
        val myLocale = Locale(languageKey)
        val dm: DisplayMetrics = resources.displayMetrics
        val conf: Configuration = resources.configuration
        conf.locale = myLocale
        resources.updateConfiguration(conf, dm)

        finish()
        startActivity(this.intent)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key == "language") {
            val languageValue: String = (sharedPreferences?.getString(key, "en")).toString()
            onChangedLanguage(languageValue);
        }
    }
}