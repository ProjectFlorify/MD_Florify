package com.example.florify.ui.welcome.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.florify.R
import com.example.florify.databinding.ActivityRegisterBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.ui.welcome.login.Login
import com.example.florify.viewmodelfactory.ViewModelFactory

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var sharedPreferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        sharedPreferencesHelper = PreferencesHelper(this)
        val apiService = ApiConfig.getApiClient()
        val repository = Repository(apiService)
        val viewModelFactory = ViewModelFactory(repository, sharedPreferencesHelper)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]

        setupObservers()
        playAnimation()
        loading()
        setupRegisterButton()
        setupGoToLoginButton()
    }

    private fun setupObservers() {
        viewModel.registrationResponse.observe(this) { response ->
            response?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                if (it.userId != null) {
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
            }
        }
        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRegisterButton() {
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (validateInputs(name, email, password)) {
                viewModel.registerUser(name, email, password)
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
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setupGoToLoginButton() {
        binding.goLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        val nameAnimator =
            ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 0f, 1f).setDuration(500)
        val emailAnimator =
            ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 0f, 1f).setDuration(500)
        val passwordAnimator =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 0f, 1f).setDuration(500)
        val buttonAnimator =
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 0f, 1f).setDuration(500)
        val togetherAnimatorSet = AnimatorSet().apply {
            playTogether(nameAnimator, emailAnimator, passwordAnimator)
        }
        val sequentialAnimatorSet = AnimatorSet().apply {
            playSequentially(togetherAnimatorSet, buttonAnimator)
        }
        sequentialAnimatorSet.start()
    }
}
