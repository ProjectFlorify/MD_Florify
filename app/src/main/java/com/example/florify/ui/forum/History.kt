package com.example.florify.ui.forum

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.florify.adapter.HistoryAdapter
import com.example.florify.api.data.PredictionsItem
import com.example.florify.databinding.ActivityHistoryBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.viewmodelfactory.ViewModelFactory

class History : AppCompatActivity(), HistoryAdapter.OnDeleteClickListener {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var sharedPreferencesHelper: PreferencesHelper
    private lateinit var viewModel: ForumViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = ApiConfig.getApiClient()
        val repository = Repository(apiService)
        sharedPreferencesHelper = PreferencesHelper(this)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository, sharedPreferencesHelper)
        )[ForumViewModel::class.java]

        binding.rvHistory.layoutManager = LinearLayoutManager(this)

        getPredictionHistory()
        observeLoading()
    }

    private fun getPredictionHistory() {
        viewModel.getHistory()
        viewModel.history.observe(this) { predictions ->
            if (predictions != null) {
                binding.rvHistory.adapter = HistoryAdapter(predictions.filterNotNull(), this)
            } else {
                Toast.makeText(this, "No History", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDeleteClick(predictionItem: PredictionsItem) {
        viewModel.deleteHistory(predictionItem.id.toString())
    }

    private fun observeLoading() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}