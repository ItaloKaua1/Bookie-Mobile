package com.example.bookie.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.bookie.R
import com.example.bookie.UserRepository
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

private fun getNomeUsuario(conversa: Conversa, id: String): String {
    return if (conversa.usuario1?.id == id) {
        conversa.usuario2?.nome.toString()
    } else {
        conversa.usuario1?.nome.toString()
    }
}

private fun getFotoUsuario(conversa: Conversa, id: String): String? {
    return if (conversa.usuario1?.id == id) {
        conversa.usuario2?.photoUrl
    } else {
        conversa.usuario1?.photoUrl
    }
}

@Composable
fun CardConversa(conversa: Conversa, onClick: (Conversa) -> Unit = {}) {
    val context = LocalContext.current
    val userRepo = UserRepository(context)
    val userId = userRepo.currentUserId.collectAsState(initial = "")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp).clickable { onClick(conversa) }
    ) {
        if (getFotoUsuario(conversa, userId.value) != null) {
            Image(
                painter = rememberAsyncImagePainter(getFotoUsuario(conversa, userId.value)),
                contentDescription = "Foto do usuário",
                modifier = Modifier.size(64.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto do usuário",
                modifier = Modifier.size(64.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f).padding(start = 8.dp)
        ) {
            Text(text = getNomeUsuario(conversa, userId.value), style = MaterialTheme.typography.titleSmall)
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