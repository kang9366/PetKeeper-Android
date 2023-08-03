package com.example.petkeeper.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE)

    var isRegistered: Boolean
        get() = pref.getBoolean("isRegistered", false)
        set(value) = pref.edit().putBoolean("isRegistered", value).apply()

    var token: String?
        get() = pref.getString("token", null)
        set(value) = pref.edit().putString("token", value).apply()
}