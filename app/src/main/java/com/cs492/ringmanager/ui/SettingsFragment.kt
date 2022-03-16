package com.cs492.ringmanager.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.cs492.ringmanager.R

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting, rootKey)
    }
}