package com.example.bookie.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookie.R
import com.example.bookie.models.Livro
import com.example.bookie.ui.theme.BookieTheme
import kotlin.math.abs

@Composable
fun CardLivroVariante(livro: Livro, mostrarAvaliacao: Boolean = false, mostrarPorcentagem: Boolean = false) {
    val width = 86;

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.capa_bunny),
            contentDescription = stringResource(id = R.string.capa_livro),
            modifier = Modifier.height(120.dp).width((width).dp),
        )
        if (mostrarAvaliacao) {
            Row(
                horizontalArrangement = Arrangement.Center,
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
            }
        }

        if (mostrarPorcentagem) {
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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    val livro = Livro("", "Teste", "Autor Teste", "Livro de Teste")

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BookieTheme {
            CardLivroVariante(livro, true)
        }
        BookieTheme {
            CardLivroVariante(livro, mostrarPorcentagem = true)
        }
    }
}