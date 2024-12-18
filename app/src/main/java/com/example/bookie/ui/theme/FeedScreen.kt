package com.example.bookie.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import com.example.bookie.R

data class Post(
    val username: String,
    val description: String,
    val likes: Int,
    val comments: Int
)

val mockPosts = listOf(
    Post("User 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", 10, 2),
    Post("User 2", "Adicionei um histórico de leitura. Lorem Ipsum Lorem Ipsum", 30, 2),
    Post("User 3", "Cras eget lorem nec ante iaculis interdum.", 15, 1)
)

@Composable
fun FeedScreen() {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar() },
        floatingActionButton = { FloatingNewPost() }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(mockPosts) { post ->
                PostCard(post)
            }
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "oi, name!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_notifications),
            contentDescription = "Notifications",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun PostCard(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = post.username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = "Bookmark"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.description,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("❤️ ${post.likes} curtidas")
                Text("💬 ${post.comments} comentários")
            }
        }
    }
}

@Composable
fun BottomNavBar() {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.ic_home), contentDescription = "Home") },
            label = { Text("home") },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.ic_search), contentDescription = "Busca") },
            label = { Text("busca") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.ic_add), contentDescription = "Novo Post") },
            label = { Text("novo post") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.ic_chat), contentDescription = "Chat") },
            label = { Text("chat") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.ic_notifications), contentDescription = "Notificações") },
            label = { Text("notificações") },
            selected = false,
            onClick = {}
        )
    }
}

@Composable
fun FloatingNewPost() {
    FloatingActionButton(onClick = { /* Novo post */ }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "Novo Post"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
    FeedScreen()
}