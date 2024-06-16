package com.example.florify.ui.ensiklopedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florify.api.data.EncyclopediaItem
import com.example.florify.repository.Repository
import kotlinx.coroutines.launch

class EncyclopediaViewModel(val repository: Repository) : ViewModel() {
    private val _encyclopediaList = MutableLiveData<List<EncyclopediaItem?>?>()
    val encyclopediaList: LiveData<List<EncyclopediaItem?>?> get() = _encyclopediaList

    private val _filteredEncyclopediaList = MutableLiveData<List<EncyclopediaItem?>?>()
    val filteredEncyclopediaList: LiveData<List<EncyclopediaItem?>?> get() = _filteredEncyclopediaList

    private val _titlesList = MutableLiveData<List<String?>?>()
    val titlesList: LiveData<List<String?>?> get() = _titlesList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    fun fetchEncyclopediaItems() {
        showLoading()
        viewModelScope.launch {
            val result = repository.getEncyclopediaList()
            result.onSuccess {
                val items = it.encyclopedia
                _encyclopediaList.value = items
                _filteredEncyclopediaList.value = items
                hideLoading()
            }.onFailure {
                _encyclopediaList.value = null
                _filteredEncyclopediaList.value = null
                _error.value = it.message
                hideLoading()
            }
        }
    }

    fun searchEncyclopedia(query: String) {
        viewModelScope.launch {
            showLoading()
            val currentList = _encyclopediaList.value
            val filteredList = currentList?.filter {
                it?.title?.contains(query, ignoreCase = true) ?: false
            }
            _filteredEncyclopediaList.value = filteredList
        }
        hideLoading()
    }

    private fun showLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }
}
