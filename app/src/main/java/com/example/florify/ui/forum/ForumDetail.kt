package com.example.florify.ui.forum

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.florify.adapter.CommentAdapter
import com.example.florify.api.data.ForumDataItem
import com.example.florify.databinding.ActivityForumDetailBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.viewmodelfactory.ViewModelFactory

class ForumDetail : AppCompatActivity() {
    private lateinit var binding: ActivityForumDetailBinding
    private lateinit var viewModel: ForumViewModel
    private lateinit var sharedPreferencesHelper: PreferencesHelper
    private lateinit var forumDetail: ForumDataItem
    private var isLiked = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityForumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = ApiConfig.getApiClient()
        val repository = Repository(apiService)
        sharedPreferencesHelper = PreferencesHelper(this)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository, sharedPreferencesHelper)
        )[ForumViewModel::class.java]

        binding.rvComment.layoutManager = LinearLayoutManager(this)
        setUpData()
        viewModel.getComment(forumDetail.id.toString())
        observeComments()
        postComment()
        observeLoading()

    }

    @Suppress("DEPRECATION")
    private fun setUpData() {
        forumDetail = intent.getParcelableExtra<ForumDataItem>("ForumItem") as ForumDataItem
        Glide.with(applicationContext)
            .load(forumDetail.imagePrediction)
            .into(binding.imageForumDetail)
        binding.user.text = forumDetail.userName
        binding.titleDetailForum.text = forumDetail.resultPrediction
        binding.caption.text = forumDetail.caption
    }

    private fun observeComments() {
        viewModel.comment.observe(this) { comments ->
            comments?.let {
                binding.rvComment.adapter = CommentAdapter(it.filterNotNull())
            } ?: run {
                Toast.makeText(this, "No comments available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postComment() {
        binding.postButton.setOnClickListener {
            val comment = binding.inputComment.text.toString().trim()
            if (comment.isNotBlank()) {
                Toast.makeText(this, "Posting comment...", Toast.LENGTH_SHORT).show()
                viewModel.postComment(forumDetail.id.toString(), comment)
                binding.inputComment.text?.clear()
                observeComments()
            } else {
                Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeLoading() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}
