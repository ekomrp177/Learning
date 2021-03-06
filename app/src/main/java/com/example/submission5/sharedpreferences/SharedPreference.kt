package com.example.submission5.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

internal  class SharedPreference(val context: Context) {
    private val PREFS_NAME = "switch"
    val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setValueBool(KEY_NAME: String, status: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(KEY_NAME, status!!)
        editor.commit()
    }

    fun getValueBool(KEY_NAME: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(KEY_NAME, defaultValue)

    }
}