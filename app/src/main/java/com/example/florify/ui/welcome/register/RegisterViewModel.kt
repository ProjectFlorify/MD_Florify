package com.example.florify.ui.welcome.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florify.api.data.RegisterResponse
import com.example.florify.repository.Repository
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: Repository) : ViewModel() {
    private val _registrationResponse = MutableLiveData<RegisterResponse?>()
    val registrationResponse: LiveData<RegisterResponse?> get() = _registrationResponse

    private val _error = MutableLiveData<String?>()

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading : LiveData<Boolean> = _isLoading
    val error: LiveData<String?> get() = _error

    fun registerUser(name: String, email: String, password: String) {
        showLoading()
        viewModelScope.launch {
            val result = userRepository.registerUser(name, email, password)
            result.onSuccess {
                hideLoading()
                _registrationResponse.postValue(it)
            }.onFailure {
                hideLoading()
                _error.postValue(it.message)
            }
        }
    }

    private fun showLoading(){
        _isLoading.value = true
    }

    private fun hideLoading(){
        _isLoading.value = false
    }
}
