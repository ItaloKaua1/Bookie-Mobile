package com.example.bookie.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.bookie.R
import com.example.bookie.models.Livro
import com.example.bookie.models.Usuario
import com.example.bookie.ui.theme.BookieTheme

@Composable
fun AvatarChat(usuario: Usuario, onClick: (Usuario) -> Unit = {}) {
    Box(
        modifier = Modifier.clickable { onClick(usuario) }
    ) {
        AsyncImage(
            model = R.drawable.avatar,
            contentDescription = null,
            modifier = Modifier.height(48.dp).width(48.dp),
        )
        Icon(
            modifier = Modifier
                .size(16.dp)
                .fillMaxWidth()
                .offset(x = 32.dp, y = 32.dp),
            tint = Color.Green,
            painter = painterResource(R.drawable.ic_circle),
            contentDescription = "ativo",
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun AvatarChatPreview() {
//    BookieTheme {
//        AvatarChat()
//    }
//}