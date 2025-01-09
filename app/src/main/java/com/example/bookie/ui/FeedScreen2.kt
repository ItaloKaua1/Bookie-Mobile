package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.R
import com.example.bookie.components.CardPost
import com.example.bookie.components.MinhasPostagens
import com.example.bookie.components.TopBar
import com.example.bookie.models.Livro
import com.example.bookie.models.VolumeInfo
import com.example.bookie.models.ImageLinks
import com.example.bookie.models.Post
import java.util.Date

private val mockFeedPosts = listOf(
    Post(
        usuario = "User 1",
        titulo = "Dica de leitura",
        texto = "Este é um excelente livro para quem gosta de ficção!",
        curtidas = 10,
        comentarios = 2,
        avaliacao = 4.5f,
        data_criacao = Date(),
        livro = Livro(
            id = "1",
            volumeInfo = VolumeInfo(
                nome = "O Nome do Vento",
                autor = listOf("Patrick Rothfuss"),
                sinopse = "Uma jornada épica cheia de mistério e magia.",
                paginas = 662,
                imageLinks = ImageLinks(
                    smallThumbnail = "https://example.com/small_thumbnail1.jpg",
                    thumbnail = "https://example.com/thumbnail1.jpg"
                )
            )
        )
    ),
    Post(
        usuario = "User 2",
        titulo = "Adorei esse livro!",
        texto = "Uma leitura que mexeu comigo de várias maneiras.",
        curtidas = 30,
        comentarios = 5,
        avaliacao = 4.0f,
        data_criacao = Date(),
        livro = Livro(
            id = "2",
            volumeInfo = VolumeInfo(
                nome = "A Guerra dos Tronos",
                autor = listOf("George R. R. Martin"),
                sinopse = "Uma história envolvente de poder, guerra e intrigas.",
                paginas = 835,
                imageLinks = ImageLinks(
                    smallThumbnail = "https://example.com/small_thumbnail2.jpg",
                    thumbnail = "https://example.com/thumbnail2.jpg"
                )
            )
        )
    )
)

private val mockUserPosts = listOf(
    Post(
        usuario = "Você",
        titulo = "Minha primeira postagem!",
        texto = "Minha primeira experiência no Bookie foi maravilhosa!",
        curtidas = 50,
        comentarios = 10,
        avaliacao = 5.0f,
        data_criacao = Date()
    ),
    Post(
        usuario = "Você",
        titulo = "Adorei compartilhar aqui.",
        texto = "Estou curtindo muito essa rede social de leitura!",
        curtidas = 30,
        comentarios = 5,
        avaliacao = 4.8f,
        data_criacao = Date()
    )
)

@Composable
fun FeedScreen(navController: NavController) {
    var isViewingMyPosts by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                onOpenDrawer = { /* Abrir drawer ou realizar ação personalizada */ }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Navegar para tela de newpost */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Adicionar novo post"
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
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

                if (isViewingMyPosts) {
                    MinhasPostagens(posts = mockUserPosts)
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    ) {
                        items(mockFeedPosts) { post ->
                            CardPost(post = post)
                        }
                    }
                }
            }
        }
    }
}