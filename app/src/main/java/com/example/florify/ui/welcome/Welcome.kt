package com.example.florify.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.florify.ui.main.MainActivity
import com.example.florify.databinding.ActivityWelcomeBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.ui.welcome.login.Login
import com.example.florify.ui.welcome.register.Register

class Welcome : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.login.setOnClickListener {
            moveActivity("login")
        }

        binding.register.setOnClickListener {
            moveActivity("register")
        }

        val sharedPreferencesHelper = PreferencesHelper(this)
        val session = sharedPreferencesHelper.getSession()
        if (session != null) {
            moveActivity("main")
        }
    }

    private fun moveActivity(action: String) {
        val intent = when (action) {
            "login" -> Intent(this, Login::class.java)
            "register" -> Intent(this, Register::class.java)
            "main" -> Intent(this, MainActivity::class.java)
            else -> throw IllegalArgumentException("Unknown action")
        }
        startActivity(intent)
    }
}