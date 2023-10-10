package com.example.petkeeper.util.api

import com.example.petkeeper.model.User
import com.example.petkeeper.util.App
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService{
    @GET("{userId}")
    fun getUserInfo(@Path("userId") userId: String): Call<User>

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

    @FormUrlEncoded
    @POST("/pet")
    fun postPetData(
        @Header("Authorization") token: String,
        @Field("PET_NAME") PET_NAME: String,
        @Field ("PET_KIND") PET_KIND: String,
        @Field ("PET_GENDER") PET_GENDER: String,
        @Field ("PET_BIRTHDATE") PET_BIRTHDATE: String
    ): Call<JsonObject>

    @POST("/user")
    fun postSignUpData(
        @Body user: User
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("/pet/weight/{id}")
    fun postWeight(
        @Header("Authorization") token: String = "Bearer ${App.preferences.token}",
        @Path("id") id: String? = App.preferences.Pet().id,
        @Field("PET_ID") PET_ID: String? = App.preferences.Pet().id,
        @Field("PET_WEIGHT") PET_WEIGHT: String,
        @Field("PET_WEIGHT_DATE") PET_WEIGHT_DATE: String
    ): Call<JsonObject>
}