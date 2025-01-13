package com.example.bookie.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.imageLoader
import coil3.util.DebugLogger
import com.example.bookie.models.Livro
import kotlin.math.abs


@Composable
fun CardLivroVariante(livro: Livro, mostrarAvaliacao: Boolean = false, mostrarPorcentagem: Boolean = false, onClick: (Livro) -> Unit = {}) {
    val width = 86;
    val imageLoader = LocalContext.current.imageLoader.newBuilder().logger(DebugLogger()).build()


    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.clickable { onClick(livro) },
    ) {
        AsyncImage(
            model = livro.getCapa(),
            contentDescription = "Capa do livro",
            modifier = Modifier.height(120.dp).width((width).dp),
            placeholder = ColorPainter(Color.Gray),
            contentScale = ContentScale.Crop,
            imageLoader = imageLoader
        )
        if (mostrarAvaliacao) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width((width).dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(12.dp)
                        .fillMaxWidth(),
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Voto",
                )
                Icon(
                    modifier = Modifier
                        .size(12.dp)
                        .fillMaxWidth(),
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Voto",
                )
                if (livro.favorito == true) {
                    Icon(
                        modifier = Modifier
                            .size(16.dp)
                            .fillMaxWidth()
                            .padding(start = 4.dp),
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Voto",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }


        if (mostrarPorcentagem) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .width((width).dp)
                            .border(width = 1.dp, color = Color.Red, shape = RoundedCornerShape(8.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .height(12.dp)
                                .width(abs(width*0.5).dp)
                                .background(Color.Red)
                        )
                    }
                    Text(text = "43%", style = MaterialTheme.typography.labelSmall)
                }
                if (livro.favorito == true) {
                    Icon(
                        modifier = Modifier
                            .size(16.dp)
                            .fillMaxWidth()
                            .padding(start = 4.dp),
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Voto",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun GreetingPreview() {
//    val livro = Livro(ImageLinks(), "Teste", arrayOf("Autor Teste"), "Livro de Teste")
//
//    Column(
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        BookieTheme {
//            CardLivroVariante(livro, true)
//        }
//        BookieTheme {
//            CardLivroVariante(livro, mostrarPorcentagem = true)
//        }
//    }
//}
