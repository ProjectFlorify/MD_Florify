package com.example.florify.ui.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florify.api.data.UpdateResponse
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import kotlinx.coroutines.launch

class UpdateViewModel(
    private val repository: Repository,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {
    private val _updating = MutableLiveData<UpdateResponse?>()
    val updating: LiveData<UpdateResponse?>
        get() = _updating
    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading


    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun updateUser(username: String, email: String, password: String) {
        val token = "authorization ${preferencesHelper.getSession()}"
        if (token.isEmpty()) {
            _updating.value = null
            return
        }

        viewModelScope.launch {
            showLoading()
            val result = repository.updateUser(token, username, email, password)
            result.onSuccess {
                hideLoading()
                _updating.value = it
            }.onFailure {
                hideLoading()
                _updating.value = null
                _error.value = it.message
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
