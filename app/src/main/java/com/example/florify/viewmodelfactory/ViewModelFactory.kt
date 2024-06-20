package com.example.florify.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.ui.camera.CameraViewModel
import com.example.florify.ui.forum.ForumViewModel
import com.example.florify.ui.ensiklopedia.EncyclopediaViewModel
import com.example.florify.ui.home.HomeViewModel
import com.example.florify.ui.main.MainViewModel
import com.example.florify.ui.profile.ProfileViewModel
import com.example.florify.ui.update.UpdateViewModel
import com.example.florify.ui.welcome.login.LoginViewModel
import com.example.florify.ui.welcome.register.RegisterViewModel

class ViewModelFactory(
    private val repository: Repository,
    private val preferencesHelper: PreferencesHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository, preferencesHelper) as T
        }
        if (modelClass.isAssignableFrom(EncyclopediaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EncyclopediaViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ForumViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForumViewModel(repository, preferencesHelper) as T
        }
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CameraViewModel(repository, preferencesHelper) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository, preferencesHelper) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel() as T
        }
        if (modelClass.isAssignableFrom(UpdateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UpdateViewModel(repository, preferencesHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
