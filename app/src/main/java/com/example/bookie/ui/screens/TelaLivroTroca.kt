package com.example.bookie.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookie.AppData
import com.example.bookie.R
import com.example.bookie.components.CardPost
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Post
import com.example.bookie.models.TrocaDisponivel

private val postsTroca: List<Post> = listOf()

private fun getTroca(id: String): TrocaDisponivel? {
    val appData = AppData.getInstance()
    val troca = appData.getTrocaDisponivel(id)

    if (troca == null) {
        return appData.getMinhasTrocaDisponivel(id)
    } else {
        return troca
    }
}

private fun getIsMeuLivro(id: String): Boolean {
    val appData = AppData.getInstance()
    val troca = appData.getTrocaDisponivel(id)

    if (troca == null) {
        return true
    } else {
        return false
    }
}

private fun proporTroca(navController: NavController, id: String?) {
    if (id != null) {
        navController.navigate("finalizarProposta/${id}")
    }
}

@Composable
fun TelaLivroTroca(navController: NavController, id: String) {
    var troca by remember { mutableStateOf(getTroca(id)) }
    var meuLivro by remember { mutableStateOf(getIsMeuLivro(id)) }
    var livro by remember { mutableStateOf(troca?.livro) }
    var titulo = if (livro !== null) livro!!.volumeInfo!!.nome else "Não encontrado"
    val getSinopse = { if (livro!!.volumeInfo!!.sinopse.isNullOrEmpty()) "" else livro!!.volumeInfo!!.sinopse!! }
    val getNome = { if (livro!!.volumeInfo!!.nome.isNullOrEmpty()) "" else livro!!.volumeInfo!!.nome!! }

    LayoutVariant(navController, titulo = if (titulo !== null) titulo else "Não encontrado") {
        if (livro != null) {
            Column {
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 16.dp
                    ),
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(156.dp)
                                .paint(
                                    painterResource(id = R.drawable.bg_livro),
                                    contentScale = ContentScale.FillBounds
                                )
                        ) {
                            if (livro!!.getCapa().isEmpty()) {
                                Image(
                                    painter = painterResource(id = R.drawable.capa_bunny),
                                    contentDescription = stringResource(id = R.string.capa_livro),
                                    modifier = Modifier
                                        .height(157.dp)
                                        .width(104.dp)
                                        .absoluteOffset(y = 28.dp),
                                )
                            } else {
                                AsyncImage(
                                    model = livro!!.getCapa(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(157.dp)
                                        .width(104.dp)
                                        .absoluteOffset(y = 28.dp),
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.padding(
                                top = 46.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 24.dp
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        text = getNome(),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    Text(
                                        text = livro!!.getAutor(),
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Row {
                                        SuggestionChip(
                                            onClick = {},
                                            label = {
                                                Text(
                                                    text = "Categoria",
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            },
                                            modifier = Modifier.height(16.dp),
                                        )
                                    }
                                }
                                Button(
                                    onClick = {
                                        proporTroca(navController, troca!!.document)
                                    },
                                    enabled = !meuLivro
                                ) {
                                    Text(text = "Propor Troca")
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = getSinopse(),
                                maxLines = 4,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
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
                                Text(
                                    text = "4.5",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                                Text(
                                    text = "(10.000 avaliações)",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                troca!!.estado?.let {
                                    SuggestionChip(
                                        onClick = {},
                                        label = {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        },
                                    )
                                }
                                troca!!.localizacao?.let {
                                    SuggestionChip(
                                        onClick = {},
                                        label = {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        },
                                    )
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                            Column {
                                troca!!.usuario?.nome?.let {
                                    Text(
                                        text = "Pertence a: $it",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                troca!!.observacao?.let {
                                    Text(
                                        text = "*obs: $it",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "resenhas",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
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
}

//@Preview(showBackground = true)
//@Composable
//private fun GreetingPreview() {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "feedScreen") {
//        composable(
//            route = "feedScreen"
//        ) {
//            FeedScreen(navController)
//        }
//    }
//
//    BookieTheme {
//        TelaLivro(navController, )
//    }
//}