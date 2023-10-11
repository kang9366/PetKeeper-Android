package com.example.petkeeper.view.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import com.example.petkeeper.R
import com.example.petkeeper.util.App
import com.example.petkeeper.view.login.LoginActivity
import com.example.petkeeper.view.main.MainActivity
import com.example.petkeeper.view.register.RegisterActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    companion object {
        private const val DELAY_TIME = 1500L
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        this.onBackPressedDispatcher.addCallback(this, callback)

        Handler(Looper.getMainLooper()).postDelayed({
            if(App.preferences.isLogin){
                initActivity(MainActivity())
            }else{
                initActivity(LoginActivity())
            }
        }, DELAY_TIME)
    }

    private fun initActivity(activity: Activity) {
        Intent(this@SplashActivity, activity::class.java).apply {
            startActivity(this)
        }
        finish()
    }
}