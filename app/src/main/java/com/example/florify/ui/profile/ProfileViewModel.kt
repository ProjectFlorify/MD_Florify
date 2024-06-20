package com.example.florify.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florify.api.data.DeleteResponse
import com.example.florify.api.data.GetUserResponse
import com.example.florify.api.data.PredictionsItem
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: Repository,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    private val _deleteAll = MutableLiveData<DeleteResponse?>()
    val deleteAll: LiveData<DeleteResponse?> get() = _deleteAll
    private val _deleteAccount = MutableLiveData<DeleteResponse?>()
    val deleteAccount: LiveData<DeleteResponse?> get() = _deleteAll

    private val _user = MutableLiveData<GetUserResponse?>()
    val user: LiveData<GetUserResponse?> get() = _user

    private val _history = MutableLiveData<List<PredictionsItem?>?>()
    val history: LiveData<List<PredictionsItem?>?> get() = _history

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error
    fun deleteAllHistory() {
        viewModelScope.launch {
            showLoading()
            val result = repository.deleteAllHistory(
                "authorization ${
                    preferencesHelper.getSession().toString()
                }"
            )
            result.onSuccess {
                hideLoading()
                _deleteAll.value = it
            }.onFailure {
                hideLoading()
                _deleteAll.value = null
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

    fun deleteAccount() {
        showLoading()
        viewModelScope.launch {
            val result = repository.deleteAccount(
                "authorization ${
                    preferencesHelper.getSession().toString()
                }"
            )
            result.onSuccess {
                hideLoading()
                _deleteAccount.value = it
            }.onFailure {
                hideLoading()
                _deleteAccount.value = null
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