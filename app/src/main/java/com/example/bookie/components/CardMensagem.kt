package com.example.bookie.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bookie.models.Mensagem

@Composable
fun CardMensagem(mensagem: Mensagem, enviadoPorMim: Boolean) {
    Column(
        horizontalAlignment = if (enviadoPorMim) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (enviadoPorMim) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            ),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = mensagem.corpo.toString(), style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text(text = mensagem.dataHora.toString(), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun GreetingPreview() {
//    BookieTheme {
//        CardMensagem(Mensagem(corpo = "TEste", usuario = Usuario(), Date()), false)
//    }
//}