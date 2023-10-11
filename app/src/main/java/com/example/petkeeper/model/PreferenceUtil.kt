package com.example.petkeeper.model

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE)

    var isLogin: Boolean
        get() = pref.getBoolean("isLogin", false)
        set(value) = pref.edit().putBoolean("isLogin", value).apply()

    var token: String?
        get() = pref.getString("token", null)
        set(value) = pref.edit().putString("token", value).apply()

    var userId: Int
        get() = pref.getInt("userId", -1)
        set(value) = pref.edit().putInt("userId", value).apply()

    inner class Pet {
        var id: String?
            get() = pref.getString("id", null)
            set(value) = pref.edit().putString("id", value).apply()

        var gender: String?
            get() = pref.getString("gender", null)
            set(value) = pref.edit().putString("gender", value).apply()

        var name: String?
            get() = pref.getString("name", null)
            set(value) = pref.edit().putString("name", value).apply()

        var birth: String?
            get() = pref.getString("birth", null)
            set(value) = pref.edit().putString("birth", value).apply()

        var age: Int
            get() = pref.getInt("age", 0)
            set(value) = pref.edit().putInt("age", value).apply()

        var weight: Int
            get() = pref.getInt("weight", 0)
            set(value) = pref.edit().putInt("weight", value).apply()

        var breed: String?
            get() = pref.getString("breed", null)
            set(value) = pref.edit().putString("breed", value).apply()
    }
}