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

        //Load default values for settings in case user hasn't selected values yet
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)

        applyTheme()
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

    private fun applyTheme() {
        val preferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val backgroundValue =
            (preferences.getString("colorBackground", "light")).toString()
        val accentValue =
            (preferences.getString("colorAccent", "red")).toString()

        super.setTheme(getPreferredTheme(backgroundValue, accentValue))
    }

    private fun getPreferredTheme(
        preferenceBackgroundThemeValue: String,
        preferenceAccentThemeValue: String
    ): Int {
        return if (preferenceAccentThemeValue == "blue") {
            when (preferenceBackgroundThemeValue) {
                "light" -> R.style.Theme_FocusWork_Blue
                "dark" -> R.style.Theme_FocusWork_DarkBlue
                else -> R.style.Theme_FocusWork_Blue
            }
        } else {
            when (preferenceBackgroundThemeValue) {
                "light" -> R.style.Theme_FocusWork
                "dark" -> R.style.Theme_FocusWork_Dark
                else -> R.style.Theme_FocusWork
            }
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
        if (key == "language") {
            val languageValue: String = (sharedPreferences?.getString(key, "en")).toString()
            onChangedLanguage(languageValue);
        } else if (key == "colorBackground" || key == "colorAccent") {
            applyTheme()

            finish()
            startActivity(this.intent)
        }
    }
}