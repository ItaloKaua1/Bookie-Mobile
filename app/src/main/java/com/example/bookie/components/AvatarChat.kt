package com.example.bookie.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.bookie.R
import com.example.bookie.models.Livro
import com.example.bookie.models.Usuario
import com.example.bookie.ui.theme.BookieTheme

@Composable
fun AvatarChat(usuario: Usuario, onClick: (Usuario) -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.width(48.dp).clickable { onClick(usuario) }
    ) {
        Box(
            modifier = Modifier.height(48.dp).width(48.dp),
        ) {
            if (usuario.photoUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(usuario.photoUrl),
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
//            Icon(
//                modifier = Modifier
//                    .size(16.dp)
//                    .fillMaxWidth()
//                    .offset(x = 32.dp, y = 32.dp),
//                tint = Color.Green,
//                painter = painterResource(R.drawable.ic_circle),
//                contentDescription = "ativo",
//            )
        }
        Text(text = usuario.nome!!, style = MaterialTheme.typography.labelSmall, maxLines = 1)
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun AvatarChatPreview() {
//    BookieTheme {
//        AvatarChat()
//    }
//}