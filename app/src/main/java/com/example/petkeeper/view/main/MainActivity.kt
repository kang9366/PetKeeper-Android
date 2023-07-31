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
        fun changeFragment(fragment: Fragment) {
            val name = intent.getStringExtra("name")
            val image = intent.getByteArrayExtra("image")

            val bundle = Bundle()
            bundle.putString("name", name)
            bundle.putByteArray("image", image)
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragment.arguments = bundle
            fragmentTransaction.replace(R.id.fragment, fragment)
            fragmentTransaction.commit()
        }

        changeFragment(MainFragment())
        binding.bottomNav.run {
            setItemSelected(R.id.home)
            setOnItemSelectedListener { item ->
                when (item){
                    R.id.home -> changeFragment(MainFragment())
                    R.id.cam -> changeFragment(CamFragment())
                    R.id.map -> changeFragment(MapFragment())
                    R.id.community -> changeFragment(CommunityFragment())
                    R.id.profile -> changeFragment(ProfileFragment())
                }
            }
        }
    }
}