package com.team06.focuswork.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.team06.focuswork.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}