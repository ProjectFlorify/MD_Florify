package com.example.florify.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.florify.adapter.HorizontalAdapter
import com.example.florify.databinding.FragmentHomeBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.viewmodelfactory.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
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
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository, sharedPreferencesHelper)
        )[HomeViewModel::class.java]
        binding.rvItem.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.rvItem.setHasFixedSize(true)
        getUser()
        getEncyclopedia()
        loading()
        return root
    }

    private fun getUser() {
        viewModel.getUser()
        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.textDashboard.text = "Hi, ${it.userData?.name}!"
            } else {
                Toast.makeText(requireContext(), "Gagal mendapatkan data", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getEncyclopedia() {
        viewModel.fetchEncyclopediaItems()
        viewModel.encyclopediaList.observe(viewLifecycleOwner) { items ->
            if (items != null) {
                val nonNullItems = items.filterNotNull()
                binding.rvItem.adapter = HorizontalAdapter(nonNullItems)
            } else {
                Toast.makeText(requireContext(), "Data is null", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loading() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}