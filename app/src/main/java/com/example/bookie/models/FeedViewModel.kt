package com.example.bookie.services

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookie.models.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    fun fetchPosts() {
        postRepository.getPosts(
            onSuccess = { fetchedPosts ->
                _posts.value = fetchedPosts
            },
            onFailure = { e ->
                Log.e("FeedViewModel", "Erro ao buscar posts: ${e.message}")
            }
        )
    }

    fun updateLikes(post: Post, newLikesCount: Int) {
        viewModelScope.launch {
            postRepository.updateLikes(
                postId = post.id,
                newLikes = newLikesCount,
                onSuccess = { fetchPosts() },
                onFailure = { e -> Log.e("FeedViewModel", "Erro ao atualizar curtidas: ${e.message}") }
            )
        }
    }
}


class FeedViewModelFactory(
    private val postRepository: PostRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            return FeedViewModel(postRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}
