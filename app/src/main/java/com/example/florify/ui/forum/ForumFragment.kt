package com.example.florify.ui.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.florify.adapter.ForumAdapter
import com.example.florify.databinding.FragmentForumBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.viewmodelfactory.ViewModelFactory

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null
    private lateinit var sharedPreferencesHelper: PreferencesHelper
    private lateinit var viewModel: ForumViewModel
    private val binding get() = _binding!!

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
        )[ForumViewModel::class.java]

        _binding = FragmentForumBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        getPredictionHistory()
        observeLoading()

        return root
    }

    private fun getPredictionHistory() {
        viewModel.getForumData()
        viewModel.forum.observe(viewLifecycleOwner) { predictions ->
            if (predictions != null) {
                binding.rvHistory.adapter = ForumAdapter(predictions.filterNotNull())
            } else {
                Toast.makeText(requireContext(), "No History", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
