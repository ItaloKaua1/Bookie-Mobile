package com.example.bookie.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookie.models.Post
import com.example.bookie.services.SavedPostsStore

@Composable
fun MinhasPostagens(posts: List<Post>, userName: String) {
    val userPosts = posts.filter { it.usuario == userName }
    val savedPosts by SavedPostsStore.savedPosts.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        if (userPosts.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(userPosts) { post ->
                    val isSaved = savedPosts.any { it.id == post.id }
                    CardPost(
                        post = post,
                        isSaved = isSaved,
                        onSaveClick = {
                            if (isSaved) {
                                SavedPostsStore.unsavePost(post)
                            } else {
                                SavedPostsStore.savePost(post)
                            }
                        }
                    )
                }
            }
        } else {
            Text(text = "Você ainda não tem postagens.")
        }
    }
}