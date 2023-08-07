package com.example.petkeeper.util

import android.app.Application
import com.example.petkeeper.model.PreferenceUtil

class App: Application() {
    companion object {
        lateinit var preferences: PreferenceUtil
    }

    override fun onCreate() {
        super.onCreate()
        preferences = PreferenceUtil(applicationContext)
    }
}