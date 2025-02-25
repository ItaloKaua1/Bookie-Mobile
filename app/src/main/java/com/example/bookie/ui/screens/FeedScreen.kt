package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookie.UserRepository
import com.example.bookie.components.CardPost
import com.example.bookie.components.NavigationDrawer
import com.example.bookie.services.FeedViewModel
import com.example.bookie.services.FeedViewModelFactory
import com.example.bookie.services.PostRepository
import com.example.bookie.services.SavedPostsRepository
import android.util.Log

@Composable
fun FeedScreen(navController: NavController) {
    val feedViewModel: FeedViewModel = viewModel(
        factory = FeedViewModelFactory(PostRepository())
    )
    val posts by feedViewModel.posts.collectAsState()
    val savedPosts by SavedPostsRepository.getSavedPostsFlow().collectAsState(initial = emptyList())

    val userRepository = UserRepository(LocalContext.current)
    val currentUserName by userRepository.currentUserName.collectAsState(initial = "")

    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        feedViewModel.fetchPosts()
    }

    NavigationDrawer(navController) {
        Scaffold(
            content = { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    Column {
                        TabRow(
                            selectedTabIndex = selectedTabIndex,
                            modifier = Modifier.fillMaxWidth(),
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                    height = 2.dp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        ) {
                            Tab(
                                selected = selectedTabIndex == 0,
                                onClick = { selectedTabIndex = 0 },
                                text = { Text("Feed Geral") }
                            )
                            Tab(
                                selected = selectedTabIndex == 1,
                                onClick = { selectedTabIndex = 1 },
                                text = { Text("Minhas Postagens") }
                            )
                        }

                        when (selectedTabIndex) {
                            0 -> {
                                if (posts.isEmpty()) {
                                    Text(
                                        text = "Nenhum post encontrado",
                                        modifier = Modifier.padding(16.dp)
                                    )
                                } else {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp)
                                    ) {
                                        items(posts) { post ->
                                            val isSaved = savedPosts.any { it.id == post.id }
                                            val isOwner = post.usuario == currentUserName
                                            CardPost(
                                                post = post,
                                                isSaved = isSaved,
                                                onClick = { navController.navigate("expandedPost/${post.id}") },
                                                onSaveClick = {
                                                    if (isSaved) {
                                                        SavedPostsRepository.unsavePost(post)
                                                    } else {
                                                        SavedPostsRepository.savePost(post)
                                                    }
                                                },
                                                isOwner = isOwner,
                                                onDelete = if (isOwner) {
                                                    {
                                                        PostRepository().deletePost(post,
                                                            onSuccess = {
                                                                Log.d("FeedScreen", "Post excluído com sucesso")
                                                                feedViewModel.fetchPosts()
                                                            },
                                                            onFailure = { e ->
                                                                Log.e("FeedScreen", "Erro ao excluir post: ${e.message}")
                                                            }
                                                        )
                                                    }
                                                } else null,
                                            )
                                        }
                                    }
                                }
                            }
                            1 -> {
                                val userPosts = posts.filter { it.usuario == currentUserName }

                                if (userPosts.isEmpty()) {
                                    Text(
                                        text = "Sem postagens ainda",
                                        modifier = Modifier.padding(16.dp)
                                    )
                                } else {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp)
                                    ) {
                                        items(userPosts) { post ->
                                            val isSaved = savedPosts.any { it.id == post.id }
                                            CardPost(
                                                post = post,
                                                isSaved = isSaved,
                                                onClick = { navController.navigate("expandedPost/${post.id}") },
                                                onSaveClick = {
                                                    if (isSaved) {
                                                        SavedPostsRepository.unsavePost(post)
                                                    } else {
                                                        SavedPostsRepository.savePost(post)
                                                    }
                                                },
                                                isOwner = true,
                                                onDelete = {
                                                    PostRepository().deletePost(post,
                                                        onSuccess = {
                                                            Log.d("FeedScreen", "Post excluído com sucesso")
                                                        },
                                                        onFailure = { e ->
                                                            Log.e("FeedScreen", "Erro ao excluir post: ${e.message}")
                                                        }
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
