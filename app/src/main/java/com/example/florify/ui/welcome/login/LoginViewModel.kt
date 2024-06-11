package com.example.florify.ui.welcome.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florify.api.data.LoginResponse
import com.example.florify.repository.Repository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: Repository) : ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> get() = _loginResponse

    private val _error = MutableLiveData<String?>()

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading
    val error: LiveData<String?> get() = _error


    fun loginUser(email: String, password: String) {
        showLoading()
        viewModelScope.launch {
            val result = userRepository.loginUser(email, password)
            result.onSuccess {
                hideLoading()
                _loginResponse.postValue(it)
            }.onFailure {
                hideLoading()
                _error.postValue(it.message)
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