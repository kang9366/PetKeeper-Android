package com.example.petkeeper

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder{
    var api: RetrofitService
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://petkeeper.co.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(RetrofitService::class.java)
    }
}