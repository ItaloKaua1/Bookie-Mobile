package com.example.bookie.services

import com.example.bookie.models.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SavedPostsStore {
    private val _savedPosts = MutableStateFlow<List<Post>>(emptyList())
    val savedPosts: StateFlow<List<Post>> get() = _savedPosts

    fun savePost(post: Post) {
        if (!isPostSaved(post)) {
            _savedPosts.value = _savedPosts.value + post
        }
    }

    fun unsavePost(post: Post) {
        _savedPosts.value = _savedPosts.value.filterNot { it.id == post.id }
    }

    fun isPostSaved(post: Post): Boolean {
        return _savedPosts.value.any { it.id == post.id }
    }
}
