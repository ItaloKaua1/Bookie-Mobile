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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.bookie.ui.theme.BookieTheme

@Composable
fun CardLivro(livro: Livro, modifier: Modifier = Modifier) {
    Card (
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
        ),
        modifier = modifier,
    ) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_local_library),
                contentDescription = stringResource(id = R.string.capa_livro),
                modifier = Modifier.height(64.dp).width(55.dp),
            )
            Column {
                Text(text = livro.nome, modifier = Modifier.padding(bottom = 2.dp))
                Text(text = livro.autor, modifier = Modifier.padding(bottom = 8.dp))
                Text(text = livro.sinopse)
            }
        }

    }
}

@Preview(showBackground = false)
@Composable
private fun GreetingPreview() {
    val livro = Livro("", "Teste", "Autor Teste", "Livro de Teste")
    BookieTheme {
        CardLivro(livro)
    }
}