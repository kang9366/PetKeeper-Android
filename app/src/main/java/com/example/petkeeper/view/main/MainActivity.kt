package com.example.petkeeper.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.petkeeper.R
import com.example.petkeeper.databinding.ActivityMainBinding
import com.example.petkeeper.util.binding.BindingActivity

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigationBar()
    }

    private fun initNavigationBar(){
        changeFragment<MainFragment>()
        binding.bottomNav.run {
            setItemSelected(R.id.home)
            setOnItemSelectedListener { item ->
                when (item){
                    R.id.home -> changeFragment<MainFragment>()
                    R.id.cam -> changeFragment<CamFragment>()
                    R.id.map -> changeFragment<MapFragment>()
                    R.id.community -> changeFragment<CommunityFragment>()
                    R.id.profile -> changeFragment<ProfileFragment>()
                }
            }
        }
    }

    private inline fun <reified T: Fragment> changeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment, T::class.java.newInstance())
            .commit()
    }
}