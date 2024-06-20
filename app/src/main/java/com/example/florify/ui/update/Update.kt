package com.example.florify.ui.update

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.florify.R
import com.example.florify.databinding.ActivityUpdateBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.ui.profile.ProfileFragment
import com.example.florify.viewmodelfactory.ViewModelFactory

class Update : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var viewModel: UpdateViewModel
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)
        val apiService = ApiConfig.getApiClient()
        val repository = Repository(apiService)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository, preferencesHelper)
        )[UpdateViewModel::class.java]

        observeViewModel()
        setupUpdateButton()
        loading()
    }

    private fun observeViewModel() {
        viewModel.updating.observe(this) { response ->
            response?.let {
                Toast.makeText(this, it.message ?: "Update successful", Toast.LENGTH_SHORT).show()
                if (it.error == null) {
                    navigateToProfile()
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupUpdateButton() {
        binding.updateButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (validateInputs(name, email, password)) {
                viewModel.updateUser(name, email, password)
            }
        }
    }

    private fun validateInputs(name: String, email: String, password: String): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.all_fields_required, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun loading() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun navigateToProfile() {
        val intent = Intent(this, ProfileFragment::class.java)
        startActivity(intent)
        finish()
    }
}

