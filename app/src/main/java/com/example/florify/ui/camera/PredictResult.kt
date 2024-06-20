package com.example.florify.ui.camera

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.florify.api.data.EncyclopediaItem
import com.example.florify.databinding.ActivityPredictResultBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.viewmodelfactory.ViewModelFactory

class PredictResult : AppCompatActivity() {
    private lateinit var binding: ActivityPredictResultBinding
    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var viewModel: CameraViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        binding = ActivityPredictResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)
        val apiService = ApiConfig.getApiClient()
        val repository = Repository(apiService)
        val viewModelFactory = ViewModelFactory(repository, preferencesHelper)
        viewModel = ViewModelProvider(this, viewModelFactory)[CameraViewModel::class.java]
        val prediction = intent.getStringExtra("EXTRA_PREDICTION") ?: "No prediction available"
        viewModel.searchResult(prediction)
        viewModel.searchEncyclopedia.observe(this) { encyclopediaList ->
            encyclopediaList?.firstOrNull()?.let { item ->
                displayEncyclopediaItem(item)
            } ?: run {
                binding.title.text = "No data available"
            }
        }
    }

    private fun displayEncyclopediaItem(item: EncyclopediaItem) {
        binding.title.text = item.title
        binding.desc.text = item.description
        Glide.with(this)
            .load(item.image)
            .into(binding.image)
        binding.handle.text = item.handling
    }
}
