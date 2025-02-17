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
import com.example.bookie.UserRepository
import com.example.bookie.models.Post

@Composable
fun MinhasPostagens(posts: List<Post>) { // Removido o parâmetro userRepository
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        if (posts.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(posts) { post ->
                    // Passa apenas o post, pois o nome do usuário já está dentro dele
                    CardPost(post = post)
                }
            }
        } else {
            Text(text = "Você ainda não tem postagens.")
        }
    }
}
