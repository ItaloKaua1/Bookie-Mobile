package com.example.bookie.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.bookie.components.CardPost
import com.example.bookie.components.LayoutVariant
import com.example.bookie.services.SavedPostsRepository

@Composable
fun SavedPostsScreen(navController: NavController) {
    val savedPosts by SavedPostsRepository.getSavedPostsFlow().collectAsState(initial = emptyList())

    LayoutVariant(navController, "Salvos", false) {
        LazyColumn {
            items(savedPosts) { post ->
                CardPost(
                    post = post,
                    isSaved = true,
                    onClick = { navController.navigate("detalhesPost/${post.id}") },
                    onSaveClick = { SavedPostsRepository.unsavePost(post) }
                )
            }
        }
    }
}
