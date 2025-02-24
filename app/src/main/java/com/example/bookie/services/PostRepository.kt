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
                val posts = result.documents.mapNotNull { document ->
                    // Converte o documento para Post e preenche o campo 'id'
                    document.toObject(Post::class.java)?.copy(id = document.id)
                }
                onSuccess(posts)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun updateLikes(
        postId: String,
        newLikes: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("posts")
            .document(postId)
            .update("curtidas", newLikes)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
