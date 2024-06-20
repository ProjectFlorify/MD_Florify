package com.example.florify.ui.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.florify.R
import com.example.florify.api.data.PostData
import com.example.florify.api.data.PredictionData
import com.example.florify.databinding.ActivityCameraResultBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.viewmodelfactory.ViewModelFactory
import java.io.File

class CameraResult : AppCompatActivity() {
    private lateinit var binding: ActivityCameraResultBinding
    private lateinit var viewModel: CameraViewModel
    private var selectedPlant: String = ""
    private var currentImageUri: Uri? = null
    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var file: File
    private lateinit var name: String
    private lateinit var predictionData: PredictionData
    private lateinit var description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        binding = ActivityCameraResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)
        val apiService = ApiConfig.getApiClient()
        val repository = Repository(apiService)
        val viewModelFactory = ViewModelFactory(repository, preferencesHelper)
        viewModel = ViewModelProvider(this, viewModelFactory)[CameraViewModel::class.java]

        val imageUri = intent.getStringExtra("imageUri")
        if (imageUri != null) {
            currentImageUri = Uri.parse(imageUri)
            binding.imageView.setImageURI(currentImageUri)
        } else {
            Toast.makeText(this, "No image available for display", Toast.LENGTH_SHORT).show()
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedPlant = when (checkedId) {
                R.id.potato -> "potato"
                R.id.rice -> "rice"
                R.id.corn -> "corn"
                else -> "potato"
            }
        }

        binding.scanButton.setOnClickListener {
            currentImageUri?.let { uri ->
                file = uriToFile(uri)
                viewModel.setPredictResponse(file, selectedPlant)
                Toast.makeText(this, "Predicting...", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(this, "No image available for prediction", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.predictResponse.observe(this) { response ->
            response?.let {
                val prediction = it.predictionData?.prediction ?: "Prediction failed"
                predictionData = it.predictionData ?: PredictionData()
                seeDetail(prediction)
            } ?: run {
                Toast.makeText(this, "Prediction response is null", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, "Error: $errorMsg", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getUser()
        viewModel.user.observe(this) {
            if (it != null) {
                name = it.userData?.name.toString()
            } else {
                Toast.makeText(this, "Failed to get user data", Toast.LENGTH_SHORT).show()
            }
        }

        binding.submitButton.setOnClickListener {
            val comment = binding.inputText.text.toString().trim()
            if (comment.isBlank()) {
                Toast.makeText(this, "Comment is required", Toast.LENGTH_SHORT).show()
            } else {
                description = comment
                postData()
            }
        }

        loading()
    }

    private fun postData() {
        val postData = PostData(
            id = predictionData.id.toString(),
            userName = name,
            imagePrediction = currentImageUri.toString(),
            plantPrediction = selectedPlant,
            resultPrediction = predictionData.prediction.toString(),
            caption = description,
            timestamp = predictionData.timestamp
        )
        viewModel.postForum(postData, predictionData.id.toString())
        viewModel.data.observe(this) { response ->
            response?.let {
                Toast.makeText(this, "Success: $response", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(this, "Post response is null", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun seeDetail(diseases: String) {
        binding.detailButton.alpha = 1f
        binding.detailButton.text = diseases
        binding.forumCard.alpha = 1f
        binding.detailButton.setOnClickListener {
            val intent = Intent(this, PredictResult::class.java)
            intent.putExtra("EXTRA_PREDICTION", diseases)
            startActivity(intent)
            finish()
        }
    }

    private fun loading() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)!!
        val file = File(cacheDir, "temp_image.jpg")
        file.outputStream().use { inputStream.copyTo(it) }
        return file
    }
}
