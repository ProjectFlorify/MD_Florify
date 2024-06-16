package com.example.florify.repository

import com.example.florify.api.data.EncyclopediaItem
import com.example.florify.api.data.EncyclopediaResponse
import com.example.florify.api.data.LoginResponse
import com.example.florify.api.data.RegisterResponse
import com.example.florify.api.data.Response
import com.example.florify.api.setapi.ApiService

class Repository(private val apiService: ApiService) {
    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Result<RegisterResponse> {
        return try {
            val response = apiService.register(name, email, password)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(
        email: String,
        password: String
    ): Result<LoginResponse> {
        return try {
            val response = apiService.login(email, password)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(token: String): Result<Response>{
        return try {
            val response = apiService.getUSer(token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getEncyclopediaList(): Result<EncyclopediaResponse> {
        return try {
            val response = apiService.getEncyclopedia()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSearchEncyclopedia(query: String): Result<EncyclopediaResponse> {
        return try {
            val response = apiService.getSearchEncyclopedia(query)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}