package com.example.petkeeper.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.petkeeper.R
import com.example.petkeeper.databinding.FragmentProfileBinding
import com.example.petkeeper.util.App
import com.example.petkeeper.util.api.RetrofitBuilder
import com.example.petkeeper.util.binding.BindingFragment
import com.example.petkeeper.view.login.LoginActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : BindingFragment<FragmentProfileBinding>(R.layout.fragment_profile, true) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.updateUserInfo?.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.apply {
                replace(R.id.fragment, UpdateUserInfo())
                addToBackStack(null)
                commit()
            }
        }

        binding?.updatePetInfo?.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.apply {
                replace(R.id.fragment, UpdatePetInfo())
                addToBackStack(null)
                commit()
            }
        }

        binding?.logoutButton?.setOnClickListener {
            initLogout()
        }
    }

    private fun initLogout() {
        RetrofitBuilder.api.userLogOut().enqueue(object: Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("logout test", response.toString())
                if(response.isSuccessful) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    activity?.finish()
                    startActivity(intent)
                    App.preferences.clearAll()
                    Toast.makeText(requireContext(), "로그아웃 되었습니다!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("logout test", t.message.toString())
            }
        })
    }
}