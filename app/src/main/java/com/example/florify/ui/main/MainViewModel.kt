package com.example.florify.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository, private val preferencesHelper: PreferencesHelper): ViewModel() {


    fun getUser(){
        viewModelScope.launch {
            val result = repository.getUser(preferencesHelper.getSession().toString())
        }
    }
}