package com.example.petkeeper.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
}