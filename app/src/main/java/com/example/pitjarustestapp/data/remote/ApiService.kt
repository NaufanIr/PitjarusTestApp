package com.example.pitjarustestapp.data.remote

import com.example.pitjarustestapp.BuildConfig
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("loginTest")
    suspend fun login (
        @Field("username") username: String,
        @Field("password") password: String
    ): Response
}