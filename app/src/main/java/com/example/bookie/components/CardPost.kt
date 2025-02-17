package com.example.bookie.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.bookie.models.ImageLinks
import com.example.bookie.models.Livro
import com.example.bookie.models.Post
import com.example.bookie.ui.theme.BookieTheme
import java.util.Date

@Composable
fun CardPost(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = post.texto, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
//            if (post.livro != null) {
//                Text(text = "Livro: ${post.livro.volumeInfo.title}", style = MaterialTheme.typography.bodySmall)
//            }
        }
    }
}

//@Preview(showBackground = false)
//@Composable
//private fun GreetingPreview() {
//    val post = Post("usuario", "Post de Teste", "Texto do post de teste", 5, 3, 4.5f, Date())
//    val livro = Livro(ImageLinks("", ""), "Livro Teste", arrayOf("Autor Teste"), "Sinopse Teste")
//    val post2 = Post("usuario", "Post de Teste", "Texto do post de teste", 5, 3, 4.5f, Date(), livro)
//
//    BookieTheme {
//        Column(
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            CardPost(post = post)
//            CardPost(post = post2)
//        }
//    }
//}