package com.example.florify.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florify.api.data.Response
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: Repository,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    private val _user = MutableLiveData<Response?>()
    val user: LiveData<Response?> get() = _user
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
}