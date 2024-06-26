package com.example.florify.api.setapi

import com.example.florify.api.data.EncyclopediaResponse
import com.example.florify.api.data.LoginResponse
import com.example.florify.api.data.RegisterResponse
import com.example.florify.api.data.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("user")
    suspend fun getUSer(
        @Header("Authorization") token: String
    ): Response

    @GET("encyclopedia")
    suspend fun getEncyclopedia(): EncyclopediaResponse

    @GET("encyclopedia/{id}")
    suspend fun getSearchEncyclopedia(
        @Path("id") id: String
    ): EncyclopediaResponse
}