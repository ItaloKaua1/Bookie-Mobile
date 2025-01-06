package com.example.bookie.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.sharp.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.bookie.AppData
import com.example.bookie.R
import com.example.bookie.components.CardPost
import com.example.bookie.models.Livro
import com.example.bookie.models.Post
import com.example.bookie.ui.theme.BookieTheme
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

val posts: List<Post> = listOf()

private fun adicionarLivro(context: Context, livro: Livro) {
    val db = FirebaseFirestore.getInstance()

    db.collection("livros").add(livro).addOnCompleteListener { it ->
        if (it.isSuccessful) {
            Toast.makeText(context, "Livro adicionado com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Desculpe, ocorreu um erro ao adicionar o livro", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun TelaLivro(navController: NavController, id: String) {
    val appData = AppData.getInstance()
    val context = LocalContext.current

    val livro = appData.getLivro(id)

    if (livro != null) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .fillMaxWidth(),
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Voltar",
                    )
                }
                Text(text = "Livro")
            }
            Card {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .paint(
                                painterResource(id = R.drawable.bg_livro),
                                contentScale = ContentScale.FillBounds
                            )
                    ) {
                        if (livro.getCapa().isEmpty()) {
                            Image(
                                painter = painterResource(id = R.drawable.capa_bunny),
                                contentDescription = stringResource(id = R.string.capa_livro),
                                modifier = Modifier.height(64.dp).width(55.dp),
                            )
                        } else {
                            AsyncImage(
                                model = livro.getCapa(),
                                contentDescription = null,
                                modifier = Modifier.height(64.dp).width(55.dp),
                            )
                        }
                    }
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(text = livro.volumeInfo?.nome!!, style = MaterialTheme.typography.labelLarge)
                                Text(text = livro.getAutor(), style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(bottom = 4.dp))
                                Row {
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text(text = "Categoria", style = MaterialTheme.typography.labelSmall) },
                                        modifier = Modifier.height(16.dp),
                                    )
                                }
                            }
                            Button(
                                onClick = {

                                    adicionarLivro(context, livro)
                                }
                            ) {
                                Text(text = "Adicionar Livro")
                            }
                        }
                        Text(text = livro.volumeInfo?.sinopse!!, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(16.dp)
                                    .fillMaxWidth(),
                                imageVector = Icons.Outlined.Star,
                                contentDescription = "Voto",
                            )
                            Text(text = "4.5", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 4.dp))
                            Text(text = "(10.000 avaliações)", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            Column {
                Text(text = "resenhas", style = MaterialTheme.typography.titleMedium)
                if (posts.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        posts.forEach { post ->
                            item {
                                CardPost(post = post)
                            }
                        }
                    }
                } else {
                    Text(text = "Ainda não existem postagens.")
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun GreetingPreview() {
//    BookieTheme {
//        TelaLivro()
//    }
//}