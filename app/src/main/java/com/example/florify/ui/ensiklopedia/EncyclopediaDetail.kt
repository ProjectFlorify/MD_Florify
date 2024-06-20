package com.example.florify.ui.ensiklopedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.florify.api.data.EncyclopediaItem
import com.example.florify.databinding.ActivityEncyclopediaDetailBinding

class EncyclopediaDetail : AppCompatActivity() {
    private lateinit var binding: ActivityEncyclopediaDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEncyclopediaDetailBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        setUpData()
    }

    @Suppress("DEPRECATION")
    private fun setUpData(){
        val encyclopediaDetail = intent.getParcelableExtra<EncyclopediaItem>("EncyclopediaItem") as EncyclopediaItem
        Glide.with(applicationContext)
            .load(encyclopediaDetail.image)
            .into(binding.image)
        binding.title.text = encyclopediaDetail.title
        binding.desc.text = encyclopediaDetail.description
        binding.handle.text = encyclopediaDetail.handling
    }
}