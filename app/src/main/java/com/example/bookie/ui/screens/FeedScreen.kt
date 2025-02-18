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
import androidx.navigation.NavController
import com.example.bookie.UserRepository
import com.example.bookie.components.CardPost
import com.example.bookie.components.NavigationDrawer
import com.example.bookie.services.FeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(navController: NavController, feedViewModel: FeedViewModel) {
    val posts by feedViewModel.posts.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val userRepo = UserRepository(context)
    val userName by userRepo.currentUserName.collectAsState(initial = "")

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
                                            CardPost(post = post)
                                        }
                                    }
                                }
                            }
                            1 -> {
                                val userPosts = posts.filter { it.usuario == userName }

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
                                            CardPost(post = post)
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