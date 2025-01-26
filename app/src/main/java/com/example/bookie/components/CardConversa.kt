package com.example.bookie.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.bookie.R
import com.example.bookie.models.Conversa
import com.example.bookie.models.Mensagem
import com.example.bookie.models.Usuario
import com.example.bookie.ui.theme.BookieTheme

private fun getLastMessage(mensagens: List<Mensagem>?): String {
    if (mensagens == null || mensagens.isEmpty()) {
        return "nenhuma mensagem"
    }

    return mensagens[mensagens.size - 1].corpo!!.toString()
}

@Composable
fun CardConversa(conversa: Conversa, onClick: (Conversa) -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp).clickable { onClick(conversa) }
    ) {
        AsyncImage(
            model = R.drawable.avatar,
            contentDescription = null,
            modifier = Modifier.height(40.dp).width(40.dp),
        )
        Column(
            modifier = Modifier.weight(1f).padding(start = 8.dp)
        ) {
            Text(text = "${conversa.usuario2!!.nome}", style = MaterialTheme.typography.titleSmall)
            Text(text = getLastMessage(conversa.mensagens), style = MaterialTheme.typography.bodySmall)
        }
//        Box(
//            modifier = Modifier
//                .clip(RoundedCornerShape(50))
//                .background(
//                    Color.Green
//                ),
//        ) {
//            Text(text = "1", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 4.dp))
//        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun P() {
//    BookieTheme {
//        CardConversa()
//    }
//}