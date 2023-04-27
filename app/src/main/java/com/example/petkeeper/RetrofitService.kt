package com.example.petkeeper

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitService{
    @GET("{userId}")
    fun getUserInfo(@Path("userId") userId: String): Call<UserInfo>
}