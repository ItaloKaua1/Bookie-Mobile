package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.components.CardPost
import com.example.bookie.components.NavigationDrawer
import com.example.bookie.services.FeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(navController: NavController, feedViewModel: FeedViewModel) {
    val posts by feedViewModel.posts.collectAsState()
    var isViewingMyPosts by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        feedViewModel.fetchPosts()
    }

    NavigationDrawer(navController) {
        Scaffold(
            content = { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Button(
                                onClick = { isViewingMyPosts = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!isViewingMyPosts) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text("Feed Geral")
                            }
                            Button(
                                onClick = { isViewingMyPosts = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isViewingMyPosts) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text("Minhas Postagens")
                            }
                        }

                        // Conteúdo da tela
                        if (isViewingMyPosts) {
                            // Tela de "Minhas Postagens"
                            if (posts.isEmpty()) {
                                Text(
                                    text = "Sem postagens ainda",
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                // Aqui você pode adicionar a lógica para filtrar e exibir apenas os posts do usuário atual
                                Text(
                                    text = "Ainda não há filtro para minhas postagens",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        } else {
                            // Tela de "Feed Geral"
                            if (posts.isEmpty()) {
                                Text(
                                    text = "Nenhum post encontrado",
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxSize().padding(16.dp)
                                ) {
                                    items(posts) { post ->
                                        CardPost(post = post)
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