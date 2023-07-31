package com.example.petkeeper.util

import android.app.Application

class PetKeeper: Application() {
    companion object {
        lateinit var preferences: PreferenceUtil
    }

    override fun onCreate() {
        preferences = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}