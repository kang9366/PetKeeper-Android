package com.example.petkeeper.util.api

import com.example.petkeeper.model.PetData
import com.example.petkeeper.model.UserInfo
import com.example.petkeeper.util.App
import com.google.gson.JsonObject
import okhttp3.MultipartBody
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
    ): Call<JsonObject>

    @Multipart
    @POST("/diag/eye")
    fun postEyeImage(
        @Header("Authorization") token: String? = App.preferences.token,
        @Part imageFile: MultipartBody.Part
    ): Call<JsonObject>

    @POST("/pet")
    fun postPetData(
        @Header("Authorization") token: String? = "Bearer " + App.preferences.token,
        @Body petData: PetData
    )

    @POST("/user")
    fun postSignUpData(
        @Body userInfo: UserInfo
    ): Call<JsonObject>
}