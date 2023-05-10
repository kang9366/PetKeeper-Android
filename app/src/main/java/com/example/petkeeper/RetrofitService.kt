package com.example.petkeeper

import retrofit2.Call
import retrofit2.http.*

interface RetrofitService{
    @GET("{userId}")
    fun getUserInfo(@Path("userId") userId: String): Call<UserInfo>

    @POST("/user/login")
    @FormUrlEncoded
    fun postLoginData(
        @Field("USER_EMAIL") user_email: String,
        @Field("USER_PASSWORD")  user_password: String
    ): Call<Void>
}