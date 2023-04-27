package com.example.petkeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.petkeeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var backKeyPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 2500){
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "종료하려면 뒤로가기를 한 번 더 누르세요", Toast.LENGTH_SHORT).show()
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2500){
            finishAffinity()
        }
    }
}