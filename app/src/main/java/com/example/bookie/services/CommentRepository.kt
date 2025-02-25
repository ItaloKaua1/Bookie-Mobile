package com.example.bookie.services

import com.example.bookie.models.Comment

class CommentRepository {
    private val commentsData = mutableMapOf<String, MutableList<Comment>>()

    suspend fun getComments(postId: String): List<Comment> {
        return commentsData[postId] ?: emptyList()
    }

    suspend fun addComment(postId: String, comment: Comment) {
        val list = commentsData.getOrPut(postId) { mutableListOf() }
        list.add(comment)
    }
}
