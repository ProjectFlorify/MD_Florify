package com.example.florify.repository

import com.example.florify.api.data.DeleteResponse
import com.example.florify.api.data.EncyclopediaResponse
import com.example.florify.api.data.ForumData
import com.example.florify.api.data.ForumDataItem
import com.example.florify.api.data.GetCommentByForumId
import com.example.florify.api.data.GetForumResponse
import com.example.florify.api.data.GetLikeResponse
import com.example.florify.api.data.HistoryResponse
import com.example.florify.api.data.LoginResponse
import com.example.florify.api.data.PredictResponse
import com.example.florify.api.data.RegisterResponse
import com.example.florify.api.data.GetUserResponse
import com.example.florify.api.data.PostCommentResponse
import com.example.florify.api.data.PostData
import com.example.florify.api.data.PostForumResponse
import com.example.florify.api.data.UpdateResponse
import com.example.florify.api.setapi.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    suspend fun getUser(token: String): Result<GetUserResponse> {
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

    suspend fun getHistory(token: String): Result<HistoryResponse> {
        return try {
            val response = apiService.getHistory(token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun predictPlantDisease(
        token: String,
        image: MultipartBody.Part,
        plant: RequestBody
    ): Result<PredictResponse> {
        return try {
            val response = apiService.predictPlantDisease(token, image, plant)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchEncyclopedia(query: String): Result<EncyclopediaResponse> {
        return try {
            val response = apiService.searchEncyclopedia(query)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteHistory(token: String, id: String): Result<DeleteResponse> {
        return try {
            val response = apiService.deleteHistory(token, id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAllHistory(token: String): Result<DeleteResponse> {
        return try {
            val response = apiService.deleteAllHistory(token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun comment(
        token: String,
        forumId: String,
        commentData: String
    ): Result<PostCommentResponse> {
        return try {
            val response = apiService.postComment(token, forumId, commentData)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAccount(token: String): Result<DeleteResponse> {
        return try {
            val response = apiService.deleteAccount(token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(
        token: String,
        name: String,
        email: String,
        password: String
    ): Result<UpdateResponse> {
        return try {
            val response = apiService.updateUser(token, name, email, password)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun postForumData(
        token: String,
        predictionId: String,
        postData: PostData
    ): Result<PostForumResponse> {
        return try {
            val response = apiService.postForumData(token, predictionId, postData)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getComment(id: String): Result<GetCommentByForumId> {
        return try {
            val response = apiService.getComment(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getForumData(): Result<GetForumResponse> {
        return try {
            val response = apiService.getForumData()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}