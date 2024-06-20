package com.example.florify.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florify.api.data.EncyclopediaItem
import com.example.florify.api.data.GetUserResponse
import com.example.florify.api.data.PostData
import com.example.florify.api.data.PostForumResponse
import com.example.florify.api.data.PredictResponse
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CameraViewModel(
    private val repository: Repository,
    val preferencesHelper: PreferencesHelper
) : ViewModel() {
    private val _predictResponse = MutableLiveData<PredictResponse?>()
    val predictResponse: MutableLiveData<PredictResponse?> get() = _predictResponse
    val error = MutableLiveData<String>()

    private val _searchEncyclopedia = MutableLiveData<List<EncyclopediaItem?>?>()
    val searchEncyclopedia: MutableLiveData<List<EncyclopediaItem?>?> get() = _searchEncyclopedia

    private val _data = MutableLiveData<PostForumResponse?>()
    val data: LiveData<PostForumResponse?> get() = _data

    private val _user = MutableLiveData<GetUserResponse?>()
    val user: LiveData<GetUserResponse?> get() = _user

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading
    fun setPredictResponse(photo: File, plant: String) {
        viewModelScope.launch {
            try {
                showLoading()
                val requestFile = photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageBody = MultipartBody.Part.createFormData("image", photo.name, requestFile)

                val plantBody = plant.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = repository.predictPlantDisease(
                    "authorization ${
                        preferencesHelper.getSession().toString()
                    }", imageBody, plantBody
                )
                if (response.isSuccess) {
                    hideLoading()
                    _predictResponse.value = response.getOrNull()
                } else {
                    hideLoading()
                    error.value = response.exceptionOrNull()?.message ?: "Unknown error"
                }
            } catch (e: Exception) {
                hideLoading()
                error.value = e.message ?: "An error occurred"
            }
        }
    }

    fun searchResult(query: String) {
        viewModelScope.launch {
            showLoading()
            val result = repository.searchEncyclopedia(query)
            result.onSuccess {
                hideLoading()
                _searchEncyclopedia.value = it.encyclopedia
            }.onFailure {
                hideLoading()
                error.value = it.message
            }
        }
    }

    fun postForum(data: PostData, predictionId: String) {
        viewModelScope.launch {
            showLoading()
            val token = "authorization ${preferencesHelper.getSession()}"
            val result = repository.postForumData(token, predictionId, data)
            result.onSuccess {
                hideLoading()
                _data.value = it
            }.onFailure {
                hideLoading()
                error.value = it.message
            }
        }
    }

    fun getUser() {
        viewModelScope.launch {
            val result =
                repository.getUser("authorization ${preferencesHelper.getSession().toString()}")
            result.onSuccess {
                _user.value = it
            }.onFailure {
                _user.value = null
            }
        }
    }

    private fun showLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }

}
