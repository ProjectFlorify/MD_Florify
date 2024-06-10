package com.example.florify.api.setapi

import com.example.florify.api.data.LoginResponse
import com.example.florify.api.data.RegisterResponse
import com.example.florify.api.data.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @GET("user")
    suspend fun getUSer(
        @Field("Authorization") token: String
    ): Response
}