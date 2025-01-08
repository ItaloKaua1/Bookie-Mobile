package com.example.bookie.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookie.R
import com.example.bookie.components.CardNotificacao
import com.example.bookie.models.Notificacao
import com.example.bookie.ui.theme.BookieTheme
import java.util.Date

@Composable
fun TelaNotificacoes() {
    var notificacoes by remember { mutableStateOf(listOf<Notificacao>()) }

    notificacoes = listOf(Notificacao("teste", "notificacao de teste", Date()), Notificacao("teste 2", "notificacao de teste 2", Date()))

    if (notificacoes.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),

            ) {
            notificacoes.forEachIndexed() { index, notificacao ->
                item{
                    CardNotificacao(notificacao)
                    if (index < notificacoes.size - 1) {
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                    }
                }
            }
        }
    } else {
        Image(
            painter = painterResource(id = R.drawable.bookie_logo),
            contentDescription = "Não há notificações",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    BookieTheme {
        TelaNotificacoes()
    }
}