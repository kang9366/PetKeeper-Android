package com.example.petkeeper.util

import android.app.Application

class App: Application() {
    companion object {
        lateinit var preferences: PreferenceUtil
    }

    override fun onCreate() {
        super.onCreate()
        preferences = PreferenceUtil(applicationContext)
    }
}