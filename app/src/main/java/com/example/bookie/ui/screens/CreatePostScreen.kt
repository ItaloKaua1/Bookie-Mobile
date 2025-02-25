package com.example.bookie.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookie.UserRepository
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Livro
import com.example.bookie.models.Post
import com.example.bookie.models.Usuario
import com.example.bookie.services.BooksRepositorio
import com.example.bookie.services.PostRepository
import java.util.Date

@Composable
fun CreatePostScreen(
    navController: NavController,
    postRepository: PostRepository,
    booksRepositorio: BooksRepositorio,
    userRepository: UserRepository
) {
    var titulo by remember { mutableStateOf("") }
    var texto by remember { mutableStateOf("") }
    var livroQuery by remember { mutableStateOf("") }
    var livros by remember { mutableStateOf<List<Livro>>(emptyList()) }
    var selectedLivro by remember { mutableStateOf<Livro?>(null) }
    var avaliacao by remember { mutableStateOf(0f) }
    var showSuggestions by remember { mutableStateOf(false) }

    val context = LocalContext.current
    // Observe o nome e a foto do usuário:
    val userNameState = userRepository.currentUserName.collectAsState(initial = "")
    val userPhotoUrlState = userRepository.currentUserPhotoUrl.collectAsState(initial = null)

    LaunchedEffect(livroQuery) {
        if (livroQuery.isNotEmpty()) {
            val response = booksRepositorio.buscarLivros(livroQuery)
            livros = response.items?.map { livro ->
                Livro(
                    id = livro.id,
                    volumeInfo = livro.volumeInfo,
                    favorito = null,
                    document = null,
                    disponivelTroca = null,
                    usuario = null
                )
            } ?: emptyList()
            showSuggestions = true
        } else {
            livros = emptyList()
            showSuggestions = false
        }
    }

    LayoutVariant(navController, "Publicar", false) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Texto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = livroQuery,
                onValueChange = { livroQuery = it },
                label = { Text("Pesquisar Livro") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (showSuggestions) {
                LazyColumn {
                    items(livros) { livro ->
                        LivroItem(
                            livro = livro,
                            onLivroSelected = {
                                selectedLivro = it
                                showSuggestions = false
                                livroQuery = ""
                            }
                        )
                    }
                }
            }

            // Exibir o livro selecionado
            selectedLivro?.let { livro ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Livro selecionado: ${livro.volumeInfo?.nome ?: "Sem título"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    AsyncImage(
                        model = livro.getCapa(),
                        contentDescription = "Capa do livro",
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Campo para avaliação
            Text("Avaliação: ${avaliacao.toInt()}/5")
            Slider(
                value = avaliacao,
                onValueChange = { avaliacao = it },
                valueRange = 0f..5f,
                steps = 4
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Cria o post utilizando os dados do usuário, incluindo a foto
                    val post = Post(
                        usuario = userNameState.value,
                        photoUrl = userPhotoUrlState.value,
                        titulo = titulo,
                        texto = texto,
                        curtidas = 0,
                        comentarios = 0,
                        avaliacao = avaliacao,
                        data_criacao = Date(),
                        livro = selectedLivro
                    )
                    postRepository.savePost(
                        post = post,
                        onSuccess = {
                            Log.d("CreatePostScreen", "Post salvo com sucesso!")
                            navController.popBackStack()
                        },
                        onFailure = { e ->
                            Log.e("CreatePostScreen", "Erro ao salvar post: ${e.message}")
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar")
            }
        }
    }
}

@Composable
fun LivroItem(livro: Livro, onLivroSelected: (Livro) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onLivroSelected(livro) } // Adicione o clickable aqui
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            // Exibir a capa do livro
            AsyncImage(
                model = livro.getCapa(),
                contentDescription = "Capa do livro",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                // Exibir o título do livro
                Text(
                    text = livro.volumeInfo?.nome ?: "Sem título",
                    style = MaterialTheme.typography.bodyMedium
                )
                // Exibir o autor do livro
                Text(
                    text = livro.getAutor(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}