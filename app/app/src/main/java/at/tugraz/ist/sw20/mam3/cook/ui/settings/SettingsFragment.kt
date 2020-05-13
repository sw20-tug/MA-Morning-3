package at.tugraz.ist.sw20.mam3.cook.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import at.tugraz.ist.sw20.mam3.cook.R

class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preference_screen, rootKey)
        }
    }
