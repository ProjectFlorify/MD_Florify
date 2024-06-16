package com.example.florify.ui.welcome.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.florify.ui.main.MainActivity
import com.example.florify.databinding.ActivityWelcomeBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.ui.welcome.login.Login

class Welcome : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferencesHelper = PreferencesHelper(this)
        val session = sharedPreferencesHelper.getSession()
        if (session != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.button.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}