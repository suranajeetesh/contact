package com.kishan.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * Created by Jeetesh Surana.
 */
class PreferenceProvider(context: Context) {
    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun putString(key: String, value: String?) {
        preference.edit()
            .putString(key, value)
            .apply()
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return preference.getString(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        preference.edit()
            .putBoolean(key, value)
            .apply()

    }

    fun getBoolean(key: String): Boolean {
        return preference.getBoolean(key, false)
    }

    fun getBoolean(key: String,defaultValue:Boolean): Boolean {
        return preference.getBoolean(key, defaultValue)
    }

    fun clearPref() {
        preference.edit().clear().apply()
    }
}
