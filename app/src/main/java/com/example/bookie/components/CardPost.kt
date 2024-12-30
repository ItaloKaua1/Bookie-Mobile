package com.example.bookie.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.sharp.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookie.R
import com.example.bookie.models.Livro
import com.example.bookie.models.Post
import com.example.bookie.ui.theme.BookieTheme
import java.util.Date

@Composable
fun CardPost(post: Post, modifier: Modifier = Modifier) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = stringResource(id = R.string.capa_livro),
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp),
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = post.usuario, style = MaterialTheme.typography.labelSmall)
                            Text(text = "@nome", style = MaterialTheme.typography.labelSmall)
                        }
                        Icon(
                            modifier = Modifier
                                .size(16.dp)
                                .fillMaxWidth(),
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Voto",
                        )
                    }
                }
                Text(text = "há 14 minutos", style = MaterialTheme.typography.labelSmall)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = post.titulo)
                Text(text = post.texto)
            }
            if (post.livro !== null) {
                Column {
                    HorizontalDivider(
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.capa_bunny),
                            contentDescription = stringResource(id = R.string.capa_livro),
                            modifier = Modifier.height(64.dp).width(55.dp),
                        )
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .fillMaxWidth(),
                                    painter = painterResource(R.drawable.ic_clock),
                                    contentDescription = "Voto",
                                )
                                Text(text = "adicionou um histórico de leitura", style = MaterialTheme.typography.labelSmall)
                            }
                            Text(text = post.livro!!.nome, style = MaterialTheme.typography.titleSmall)
                            Text(text = post.livro!!.autor, style = MaterialTheme.typography.labelSmall)
                            Text(text = post.livro!!.sinopse, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(16.dp)
                                .fillMaxWidth(),
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Voto",
                        )
                        Text(text = "${post.curtidas} curtidas", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(start = 4.dp))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(16.dp)
                                .fillMaxWidth(),
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "Voto",
                        )
                        Text(text = "${post.comentarios} comentários", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(start = 4.dp))
                    }
                }
                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .fillMaxWidth(),
                    imageVector = Icons.Sharp.ThumbUp,
                    contentDescription = "Voto",
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun GreetingPreview() {
    val post = Post("usuario", "Post de Teste", "Texto do post de teste", 5, 3, 4.5f, Date())
    val livro = Livro("", "Livro Teste", "Autor Teste", "Sinopse Teste")
    val post2 = Post("usuario", "Post de Teste", "Texto do post de teste", 5, 3, 4.5f, Date(), livro)

    BookieTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CardPost(post = post)
            CardPost(post = post2)
        }
    }
}