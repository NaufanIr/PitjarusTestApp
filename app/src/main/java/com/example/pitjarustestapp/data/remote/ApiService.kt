package com.example.pitjarustestapp.data.remote

import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("loginTest")
    suspend fun login (
        @Field("username") username: String,
        @Field("password") password: String
    ): Response
}