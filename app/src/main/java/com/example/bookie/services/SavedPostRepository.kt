package com.example.bookie.services

import com.example.bookie.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object SavedPostsRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserSavedPostsCollection() =
        auth.currentUser?.uid?.let { uid ->
            firestore.collection("users").document(uid).collection("savedPosts")
        }

    fun savePost(post: Post) {
        getUserSavedPostsCollection()?.document(post.id)?.set(post)
    }

    fun unsavePost(post: Post) {
        getUserSavedPostsCollection()?.document(post.id)?.delete()
    }

    fun getSavedPostsFlow(): Flow<List<Post>> = callbackFlow {
        val collection = getUserSavedPostsCollection()
        if (collection == null) {
            close(Throwable("Usuário não autenticado"))
            return@callbackFlow
        }

        val subscription = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val posts = snapshot?.documents?.mapNotNull { it.toObject(Post::class.java) } ?: emptyList()
            trySend(posts)
        }
        awaitClose { subscription.remove() }
    }

    suspend fun isPostSaved(post: Post): Boolean {
        val collection = getUserSavedPostsCollection() ?: return false
        val snapshot = collection.document(post.id).get().await()
        return snapshot.exists()
    }
}
