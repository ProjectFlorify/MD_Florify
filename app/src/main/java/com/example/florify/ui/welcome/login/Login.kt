package com.example.florify.ui.welcome.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.florify.ui.main.MainActivity
import com.example.florify.R
import com.example.florify.databinding.ActivityLoginBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.ui.welcome.register.Register
import com.example.florify.viewmodelfactory.ViewModelFactory

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPreferencesHelper: PreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesHelper = PreferencesHelper(this)
        val apiService = ApiConfig.getApiClient()
        val repository = Repository(apiService)
        val viewModelFactory = ViewModelFactory(repository, sharedPreferencesHelper)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        playAnimation()

        setupLoginButton()
        setupObservers()
        loading()
        goToRegister()
    }

    private fun setupObservers() {
        viewModel.loginResponse.observe(this) { response ->
            response?.let {
                if (it.token != null) {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    sharedPreferencesHelper.saveSession(it.token.toString())
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (validateInputs(email, password)) {
                viewModel.loginUser(email, password)
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
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

    private fun goToRegister() {
        binding.goRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        val emailAnimator =
            ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 0f, 1f).setDuration(500)
        val passwordAnimator =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 0f, 1f).setDuration(500)
        val buttonAnimator =
            ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).setDuration(500)
        val togetherAnimatorSet = AnimatorSet().apply {
            playTogether(emailAnimator, passwordAnimator)
        }
        val sequentialAnimatorSet = AnimatorSet().apply {
            playSequentially(togetherAnimatorSet, buttonAnimator)
        }
        sequentialAnimatorSet.start()
    }
}