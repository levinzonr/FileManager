package cz.levinzonr.filemanager.view.preferences

import android.content.Intent
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import cz.levinzonr.filemanager.R


class PreferencesFragment : PreferenceFragmentCompat() {

    companion object {
        const val PREF_DIR = "pref_default_dir"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_app, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
      return  when (preference?.key) {
            PREF_DIR -> {
                true
            }
            else ->  super.onPreferenceTreeClick(preference)
        }
    }

}