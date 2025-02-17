package com.example.bookie.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookie.models.Livro
import com.example.bookie.models.Post
import com.example.bookie.services.BooksRepositorio
import com.example.bookie.services.PostRepository
import java.util.Date

@Composable
fun CreatePostScreen(
    navController: NavController,
    postRepository: PostRepository,
    booksRepositorio: BooksRepositorio
) {
    var titulo by remember { mutableStateOf("") }
    var texto by remember { mutableStateOf("") }
    var livroQuery by remember { mutableStateOf("") }
    var livros by remember { mutableStateOf<List<Livro>>(emptyList()) }
    var selectedLivro by remember { mutableStateOf<Livro?>(null) }
    var avaliacao by remember { mutableStateOf(0f) }

    // Busca livros na API do Google Books quando o texto da pesquisa muda
//    LaunchedEffect(livroQuery) {
//        if (livroQuery.isNotEmpty()) {
//            val response = booksRepositorio.buscarLivros(livroQuery)
//            livros = response.items?.toList() ?: emptyList() // Conversão de Array para List
//        } else {
//            livros = emptyList()
//        }
//    }

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

//        LazyColumn {
//            items(livros) { livro ->
//                LivroItem(
//                    livro = livro,
//                    onLivroSelected = { selectedLivro = it }
//                )
//            }
//        }

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
                val post = Post(
                    usuario = "Usuário Atual", // Substitua pelo usuário logado
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


//@Composable
//fun LivroItem(livro: Livro, onLivroSelected: (Livro) -> Unit) {
//    Card(
//        onClick = { onLivroSelected(livro) },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(8.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Exibe a capa do livro
//            AsyncImage(
//                model = livro.volumeInfo.imageLinks?.getCapa(),
//                contentDescription = "Capa do Livro",
//                modifier = Modifier
//                    .size(64.dp)
//                    .padding(end = 8.dp)
//            )
//            // Detalhes do livro
//            Column {
//                Text(
//                    text = livro.volumeInfo.title,
//                    style = MaterialTheme.typography.titleSmall,
//                    modifier = Modifier.padding(bottom = 4.dp)
//                )
//                Text(
//                    text = livro.getAutor(),
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//        }
//    }
//}