package com.example.bookie.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookie.models.Comment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel(private val repository: CommentRepository) : ViewModel() {

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    fun fetchComments(postId: String) {
        viewModelScope.launch {
            try {
                val list = repository.getComments(postId)
                _comments.value = list
            } catch (e: Exception) {
            }
        }
    }

    fun addComment(postId: String, comment: Comment) {
        viewModelScope.launch {
            try {
                repository.addComment(postId, comment)
                _comments.value = repository.getComments(postId)
            } catch (e: Exception) {
            }
        }
    }
}


class CommentViewModelFactory(
    private val repository: CommentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}