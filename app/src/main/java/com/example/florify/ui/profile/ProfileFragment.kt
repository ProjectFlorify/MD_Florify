package com.example.florify.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.florify.api.setapi.ApiConfig
import com.example.florify.databinding.FragmentProfileBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.ui.home.HomeViewModel
import com.example.florify.ui.welcome.onboarding.Welcome
import com.example.florify.viewmodelfactory.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private lateinit var viewModel: HomeViewModel

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferencesHelper: PreferencesHelper by lazy {
            PreferencesHelper(requireContext())
        }
        val apiService = ApiConfig.getApiClient()
        val repository = Repository(apiService)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository, sharedPreferencesHelper)
        )[HomeViewModel::class.java]
        binding.logout.setOnClickListener {
            if (sharedPreferencesHelper.getSession().isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Session Does'nt Exist", Toast.LENGTH_SHORT).show()
            } else {
                sharedPreferencesHelper.removeSession()
                startActivity(Intent(requireContext(), Welcome::class.java))
            }
        }
        getUser()

        return root
    }

    private fun getUser() {
        viewModel.getUser()
        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.name.text = it.userData?.name
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}