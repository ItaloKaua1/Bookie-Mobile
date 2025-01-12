package com.example.bookie.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookie.R
import com.example.bookie.models.Notificacao
import com.example.bookie.ui.theme.BookieTheme
import java.util.Date

@Composable
fun CardNotificacao(notificacao: Notificacao) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_local_library),
            contentDescription = "Avatar",
            modifier = Modifier.height(48.dp).width(48.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(text = notificacao.usuarioOrigem!!, style = MaterialTheme.typography.titleMedium)
            Text(text = notificacao.corpo!!, style = MaterialTheme.typography.bodySmall)
            Text(text = notificacao.dataHora.toString(), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        Icon(
            modifier = Modifier
                .size(12.dp)
                .fillMaxWidth(),
            imageVector = Icons.Default.Info,
            tint = Color.Green,
            contentDescription = "Não lida",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    val notificacao = Notificacao(usuarioOrigem = "teste", corpo =  "notificacao de teste", dataHora =  Date())
    BookieTheme {
        CardNotificacao(notificacao)
    }
}