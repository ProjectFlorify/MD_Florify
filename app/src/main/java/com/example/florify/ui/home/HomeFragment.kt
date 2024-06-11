package com.example.florify.ui.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.florify.api.setapi.ApiConfig
import com.example.florify.databinding.FragmentHomeBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.ui.main.MainActivity
import com.example.florify.ui.profile.ProfileViewModel
import com.example.florify.viewmodelfactory.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var sharedPreferencesHelper: PreferencesHelper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val apiService = ApiConfig.getApiClient()
        val repository = Repository(apiService)

        sharedPreferencesHelper = PreferencesHelper(requireContext())
        viewModel = ViewModelProvider(this, ViewModelFactory(repository, sharedPreferencesHelper))[HomeViewModel::class.java]

        getUser()

        return root
    }

    private fun getUser() {
        viewModel.getUser()
        viewModel.user.observe(viewLifecycleOwner) {
            if(it != null) {
                binding.textDashboard.text = it.userData?.name
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}