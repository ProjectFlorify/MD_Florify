package com.example.florify.api.setapi

import com.example.florify.api.data.CommentsItem
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
    ): GetUserResponse

    @GET("encyclopedia")
    suspend fun getEncyclopedia(): EncyclopediaResponse

    @GET("forum")
    suspend fun getForumData(): GetForumResponse

    @GET("predict/user")
    suspend fun getHistory(
        @Header("Authorization") token: String
    ): HistoryResponse

    @DELETE("predict/delete/{id}")
    suspend fun deleteHistory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DeleteResponse

    @DELETE("predict/deleteAll")
    suspend fun deleteAllHistory(
        @Header("Authorization") token: String
    ): DeleteResponse

    @DELETE("user/delete")
    suspend fun deleteAccount(
        @Header("Authorization") token: String
    ): DeleteResponse

    @FormUrlEncoded
    @PATCH("user/update")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): UpdateResponse

    @POST("forum/{predictionId}")
    suspend fun postForumData(
        @Header("Authorization") authorization: String,
        @Path("predictionId") predictionId: String,
        @Body postData: PostData
    ): PostForumResponse

    @FormUrlEncoded
    @POST("forum/{forumId}/comment")
    suspend fun postComment(
        @Header("Authorization") authorization: String,
        @Path("forumId") forumId: String,
        @Field("comment") comment: String
    ): PostCommentResponse

    @Multipart
    @POST("predict")
    suspend fun predictPlantDisease(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("plant") plant: RequestBody
    ): PredictResponse

    @GET("encyclopedia/search")
    suspend fun searchEncyclopedia(
        @Query("q") query: String
    ): EncyclopediaResponse

    @GET("forum/{forumId}")
    suspend fun getComment(
        @Path("forumId") forumId: String
    ): GetCommentByForumId
}