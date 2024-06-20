package com.example.florify.ui.forum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.florify.api.data.CommentsItem
import com.example.florify.api.data.ForumDataItem
import com.example.florify.api.data.GetLikeResponse
import com.example.florify.api.data.PredictionsItem
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import kotlinx.coroutines.launch

class ForumViewModel(
    private val repository: Repository,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {
    private val _history = MutableLiveData<List<PredictionsItem?>?>()
    val history: LiveData<List<PredictionsItem?>?> get() = _history

    private val _forum = MutableLiveData<List<ForumDataItem?>?>()
    val forum: LiveData<List<ForumDataItem?>?> get() = _forum

    private val _comment = MutableLiveData<List<CommentsItem?>?>()
    val comment: LiveData<List<CommentsItem?>?> get() = _comment

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    fun getHistory() {
        viewModelScope.launch {
            showLoading()
            val result =
                repository.getHistory("authorization ${preferencesHelper.getSession().toString()}")
            result.onSuccess {
                hideLoading()
                _history.value = it.predictions
            }.onFailure {
                hideLoading()
                _error.value = it.message
                _history.value = null
            }
        }
    }

    fun getForumData() {
        viewModelScope.launch {
            showLoading()
            val result =
                repository.getForumData()
            result.onSuccess {
                hideLoading()
                _forum.value = it.forumData
            }.onFailure {
                hideLoading()
                _error.value = it.message
                _forum.value = null
            }
        }
    }

    fun getComment(id: String) {
        viewModelScope.launch {
            showLoading()
            val result = repository.getComment(id)
            result.onSuccess {
                hideLoading()
                _comment.value = it.forumData?.comments
            }.onFailure {
                hideLoading()
                _error.value = it.message
                _comment.value = null
            }
        }
    }

    fun deleteHistory(id: String) {
        viewModelScope.launch {
            showLoading()
            val result =
                repository.deleteHistory(
                    "authorization ${
                        preferencesHelper.getSession().toString()
                    }", id
                )

            result.onSuccess {
                getHistory()
                hideLoading()
            }.onFailure {
                hideLoading()
                _error.value = it.message
            }

        }
    }

    fun postComment(id: String, comment: String) {
        viewModelScope.launch {
            showLoading()
            val result = repository.comment(
                "authorization ${preferencesHelper.getSession().toString()}",
                id,
                comment
            )
            result.onSuccess {
                getForumData()
                _comment.value = listOf(it.commentData)
                hideLoading()
            }.onFailure {
                hideLoading()
                _error.value = it.message
            }
        }
    }


    private fun showLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }
}
