package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookie.UserRepository
import com.example.bookie.components.CardPost
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Comment
import com.example.bookie.services.CommentRepository
import com.example.bookie.services.CommentViewModel
import com.example.bookie.services.CommentViewModelFactory
import com.example.bookie.services.FeedViewModel
import com.example.bookie.services.FeedViewModelFactory
import com.example.bookie.services.PostRepository

@Composable
fun ExpandedPostScreen(
    postId: String,
    navController: NavController,
    feedViewModel: FeedViewModel = viewModel(factory = FeedViewModelFactory(PostRepository())),
    commentViewModel: CommentViewModel = viewModel(factory = CommentViewModelFactory(CommentRepository())),
    userRepository: UserRepository
) {
    LaunchedEffect(postId) {
        feedViewModel.fetchPosts()
    }

    val posts by feedViewModel.posts.collectAsState()
    val post = posts.find { it.id == postId }

    val userName by userRepository.currentUserName.collectAsState(initial = "Usuário Atual")

    if (post == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    var newCommentText by remember { mutableStateOf("") }
    val comments by commentViewModel.comments.collectAsState()

    var liked by remember { mutableStateOf(false) }
    var currentLikes by remember { mutableStateOf(post.curtidas) }

    LaunchedEffect(post.id) {
        commentViewModel.fetchComments(post.id)
    }

    LayoutVariant(navController, "Detalhe Post", false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CardPost(post = post)

            Spacer(modifier = Modifier.height(16.dp))

            // Botão de curtida
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    liked = !liked
                    currentLikes = if (liked) {
                        feedViewModel.updateLikes(post, post.curtidas + 1)
                        post.curtidas + 1
                    } else {
                        feedViewModel.updateLikes(post, post.curtidas - 1)
                        post.curtidas - 1
                    }
                }) {
                    Icon(
                        imageVector = if (liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Curtir",
                        tint = if (liked) Color.Red else Color.Gray
                    )
                }
                Text(text = "$currentLikes curtidas")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Comentários", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(comments) { comment ->
                    CommentItem(comment = comment)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = newCommentText,
                    onValueChange = { newCommentText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escreva um comentário...") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (newCommentText.isNotBlank()) {
                        val usuarioAtual = userName
                        val comment = Comment(
                            usuario = usuarioAtual,
                            texto = newCommentText
                        )
                        commentViewModel.addComment(post.id, comment)
                        newCommentText = ""
                    }
                }) {
                    Text("Enviar")
                }
            }

        }
    }
}


@Composable
fun CommentItem(comment: Comment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = comment.usuario, style = MaterialTheme.typography.titleSmall)
            Text(text = comment.texto, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
