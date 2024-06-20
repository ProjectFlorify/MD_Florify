package com.example.florify.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florify.api.data.EncyclopediaItem
import com.example.florify.api.data.GetUserResponse
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: Repository,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    private val _encyclopediaList = MutableLiveData<List<EncyclopediaItem?>?>()
    val encyclopediaList: LiveData<List<EncyclopediaItem?>?> get() = _encyclopediaList

    private val _user = MutableLiveData<GetUserResponse?>()
    val user: LiveData<GetUserResponse?> get() = _user

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error
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

    fun fetchEncyclopediaItems() {
        showLoading()
        viewModelScope.launch {
            val result = repository.getEncyclopediaList()
            result.onSuccess {
                val items = it.encyclopedia?.take(6)
                _encyclopediaList.value = items
                hideLoading()
            }.onFailure {
                _encyclopediaList.value = null
                _error.value = it.message
                hideLoading()
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