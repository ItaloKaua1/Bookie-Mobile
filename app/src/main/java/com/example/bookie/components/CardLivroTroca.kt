package com.example.bookie.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import coil3.compose.AsyncImage
import com.example.bookie.R
import com.example.bookie.models.ImageLinks
import com.example.bookie.models.Livro
import com.example.bookie.models.TrocaDisponivel
import com.example.bookie.ui.theme.BookieTheme

@Composable
fun CardLivroTroca(troca: TrocaDisponivel, onClick: (TrocaDisponivel) -> Unit = {}) {
    val livro = troca.livro
    val getSinopse = { if (livro?.volumeInfo!!.sinopse.isNullOrEmpty()) "" else livro.volumeInfo!!.sinopse!! }
    val getNome = { if (livro?.volumeInfo!!.nome.isNullOrEmpty()) "" else livro.volumeInfo!!.nome!! }

    Card (
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.outline,
        ),
        modifier = Modifier.fillMaxWidth().clickable { onClick(troca) },
    ) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            if (livro != null) {
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
                Text(text = getNome(), style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(bottom = 2.dp))
                if (livro != null) {
                    Text(text = livro.getAutor(), style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(bottom = 8.dp))
                }
                Text(text = getSinopse(), maxLines = 2, style = MaterialTheme.typography.bodySmall)
                if (troca.localizacao != null) {
                    Row {
                        Icon(
                            modifier = Modifier
                                .size(16.dp)
                                .fillMaxWidth()
                                .offset(x = 32.dp, y = 32.dp),
//                            tint = Color.Green,
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "localização",
                        )
                        Text(text = troca.localizacao!!, maxLines = 2, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//private fun GreetingPreview() {
//    val livro = Livro(ImageLinks(), "Teste", arrayOf("Autor Teste"), "Livro de Teste")
//    BookieTheme {
//        CardLivro(livro)
//    }
//}