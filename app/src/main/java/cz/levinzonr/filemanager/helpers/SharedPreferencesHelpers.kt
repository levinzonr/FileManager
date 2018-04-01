package cz.levinzonr.filemanager.helpers

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log

class SharedPreferencesHelpers(context: Context) {
    private val default = Environment.getExternalStorageDirectory().absolutePath
    private val preferences = (PreferenceManager.getDefaultSharedPreferences(context))
    init {
        Log.d("Prefs", defaultPath())
    }

    companion object {
        const val DIR_PREF = "pref_default_dir"
    }

    private fun update(key: String, path: String) {
        preferences.edit()
                .putString(key, path)
                .apply()
    }

    fun initPreferences() {
        update(DIR_PREF, default)
    }

    fun updatePath(path: String) {
        update(DIR_PREF, path)
    }

    fun defaultPath() : String {
       return preferences.getString(DIR_PREF, default)
    }

}