package com.example.florify.ui.ensiklopedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.florify.api.setapi.ApiConfig
import com.example.florify.databinding.FragmentEnsiklopediaBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.adapter.EncyclopediaAdapter
import com.example.florify.viewmodelfactory.ViewModelFactory

class EncyclopediaFragment : Fragment() {

    private var _binding: FragmentEnsiklopediaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EncyclopediaViewModel
    private lateinit var sharedPreferencesHelper: PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val apiService = ApiConfig.getApiClient()
        val repository = Repository(apiService)

        sharedPreferencesHelper = PreferencesHelper(requireContext())
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository, sharedPreferencesHelper)
        )[EncyclopediaViewModel::class.java]

        _binding = FragmentEnsiklopediaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvEncyclopedia.layoutManager = LinearLayoutManager(requireContext())

        getEncyclopedia()
        loading()
        setupSearchView()

        return root
    }

    private fun getEncyclopedia() {
        viewModel.fetchEncyclopediaItems()
        viewModel.encyclopediaList.observe(viewLifecycleOwner) { items ->
            if (items != null) {
                val nonNullItems = items.filterNotNull()
                binding.rvEncyclopedia.adapter = EncyclopediaAdapter(nonNullItems)
            } else {
                Toast.makeText(requireContext(), "Data is null", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                query?.let {
                    loading()
                    viewModel.searchEncyclopedia(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        viewModel.filteredEncyclopediaList.observe(viewLifecycleOwner) { filteredItems ->
            if (filteredItems != null) {
                val nonNullItems = filteredItems.filterNotNull()
                binding.rvEncyclopedia.adapter = EncyclopediaAdapter(nonNullItems)
            } else {
                Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT).show()
            }
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
