package com.example.bookie.services

import com.example.bookie.models.Post
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class PostRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    fun savePost(post: Post, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("posts")
            .add(post)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun getPosts(onSuccess: (List<Post>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("posts")
            .orderBy("data_criacao", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val posts = result.documents.mapNotNull { it.toObject(Post::class.java) }
                onSuccess(posts)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}