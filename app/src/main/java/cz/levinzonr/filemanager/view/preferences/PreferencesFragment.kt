package cz.levinzonr.filemanager.view.preferences

import android.content.Context
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import cz.levinzonr.filemanager.R
import cz.levinzonr.filemanager.helpers.PreferenceHelper


class PreferencesFragment : PreferenceFragmentCompat(), PreferencesMvpView {

    private lateinit var listener: OnPreferenceFragmentInteraction
    private  lateinit var dirPreference: Preference
    companion object {
        const val PREF_DIR = "pref_default_dir"
    }

    interface OnPreferenceFragmentInteraction {
        fun onSelect()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_app, rootKey)
        dirPreference = findPreference(PREF_DIR)
        dirPreference.summary = PreferenceHelper(activity.applicationContext).defaultPath()

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as OnPreferenceFragmentInteraction

    }


    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
      return  when (preference?.key) {
            PREF_DIR -> {
                listener.onSelect()
                true
            }
            else ->  super.onPreferenceTreeClick(preference)
        }
    }

    override fun onDirectoryChanged(value: String) {
        dirPreference.summary = value
    }
}