package com.example.bookie.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.bookie.models.Post
import java.text.SimpleDateFormat
import java.util.*

fun Date.formatarData(): String {
    val formato = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
    return formato.format(this)
}

@Composable
fun CardPost(
    post: Post,
    onClick: () -> Unit = {},
    isSaved: Boolean,
    onSaveClick: (Post) -> Unit,
    isOwner: Boolean = false,
    onDelete: (() -> Unit)? = null
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Cabeçalho do post
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (!post.photoUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = post.photoUrl,
                        contentDescription = "Foto do usuário",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Foto do usuário",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = post.usuario,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                // Se o post for do usuário logado, exibe o menu de opções
                if (isOwner && onDelete != null) {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Opções do post"
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Excluir post") },
                                onClick = {
                                    menuExpanded = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Conteúdo do post
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = post.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = post.texto,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Avaliação: ${post.avaliacao}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Exibe a capa do livro, se houver
            post.livro?.let { livro ->
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = livro.getCapa(),
                    contentDescription = "Capa do livro",
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = post.data_criacao.formatarData(),
                    style = MaterialTheme.typography.bodySmall
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${post.curtidas} curtidas",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Bookmark
                        else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (isSaved) "Post salvo" else "Salvar post",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onSaveClick(post) }
                    )
                }
            }
        }
    }
}
