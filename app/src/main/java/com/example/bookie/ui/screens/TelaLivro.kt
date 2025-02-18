package com.example.bookie.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.bookie.AppData
import com.example.bookie.R
import com.example.bookie.UserRepository
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Livro
import com.example.bookie.models.Post
import com.example.bookie.models.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.material.icons.filled.Check


val posts: List<Post> = listOf()

private fun adicionarLivro(context: Context, livro: Livro) {
    val db = FirebaseFirestore.getInstance()

    db.collection("livros").add(livro).addOnCompleteListener { it ->
        if (it.isSuccessful) {
            Toast.makeText(context, "Livro adicionado com sucesso!", LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Desculpe, ocorreu um erro ao adicionar o livro", LENGTH_SHORT).show()
        }
    }
}

private fun favoritarLivro(context: Context, livro: Livro, onFavoritado: () -> Unit, onError: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val documentId = livro.document
    if (documentId != null) {
        livro.favorito = true
        db.collection("livros").document(documentId).set(livro).addOnCompleteListener { it ->
            if (it.isSuccessful) {
                Toast.makeText(context, "Livro favoritado com sucesso!", LENGTH_SHORT).show()
                onFavoritado()
            } else {
                Toast.makeText(context, "Desculpe, ocorreu um erro ao favoritar o livro", LENGTH_SHORT).show()
                livro.favorito = false
                onError()
            }
        }
    }
}

private fun desfavoritarLivro(context: Context, livro: Livro, onDesfavoritado: () -> Unit, onError: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val documentId = livro.document
    if (documentId != null) {
        livro.favorito = false
        db.collection("livros").document(documentId).set(livro).addOnCompleteListener { it ->
            if (it.isSuccessful) {
                Toast.makeText(context, "Livro removido dos favoritos!", LENGTH_SHORT).show()
                onDesfavoritado()
            } else {
                Toast.makeText(context, "Desculpe, ocorreu um erro ao remover o livro dos favoritos", LENGTH_SHORT).show()
                livro.favorito = true
                onError()
            }
        }
    }
}

private fun favoritarDesfavoritarLivro(context: Context, livro: Livro, onFavoritado: () -> Unit, onDesfavoritado: () -> Unit, onError: () -> Unit) {
    if (livro.favorito == true) {
        desfavoritarLivro(context, livro, onDesfavoritado, onError)
    } else {
        favoritarLivro(context, livro, onFavoritado, onError)
    }
}

private fun getLivro(id: String, estante: Boolean? = false): Livro? {
    val appData = AppData.getInstance()

    return if (estante == true) {
        appData.getLivroEstante(id)
    } else {
        appData.getLivroBusca(id)
    }
}

private fun trocarLivro(navController: NavController, id: String?, estante: Boolean? = false) {
    if (id != null) {
        navController.navigate("disponibilizarParaTrocaScreen/${id}/${estante}")
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun TelaLivro(navController: NavController, id: String, estante: Boolean? = false) {
    val appData = AppData.getInstance()
    val context = LocalContext.current

    var livro by remember(key1 = "favorito") { mutableStateOf(getLivro(id, estante)) }
    var titulo = if (livro !== null) livro!!.volumeInfo!!.nome else "Não encontrado"
    var icon by remember { mutableStateOf(Icons.Filled.Favorite) }
    var isLoading by remember { mutableStateOf(false) }
    val getSinopse = { if (livro!!.volumeInfo!!.sinopse.isNullOrEmpty()) "" else livro!!.volumeInfo!!.sinopse!! }
    val getNome = { if (livro!!.volumeInfo!!.nome.isNullOrEmpty()) "" else livro!!.volumeInfo!!.nome!! }
    val db = FirebaseFirestore.getInstance()
    var estaNaEstante by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }  // Controle do menu suspenso
    var selectedStatus by remember { mutableStateOf(livro?.status ?: "Quero Ler") }  // Estado inicial do livro

    val statusOptions = listOf("Lido", "Lendo", "Quero Ler")

    val onAdicionarLivro = { ->
        GlobalScope.launch {
            val userRepo = UserRepository(context)
            val userName = userRepo.currentUserName.first()
            var userEmail = userRepo.currentUserEmail.first()
            var userId = userRepo.currentUserId.first()
            val usuario = Usuario(userId, userEmail, userName)

            if (livro != null) {
                livro!!.usuario = usuario
                adicionarLivro(context, livro!!)
            }
        }
    }

    LaunchedEffect(livro) {
        if (livro!!.favorito == true) {
            icon = Icons.Filled.Favorite
        } else {
            icon = Icons.Filled.FavoriteBorder
        }
    }
    LaunchedEffect(livro) {
        val idLivro = livro?.id

        if (idLivro != null) {
            db.collection("livros").whereEqualTo("id", idLivro).get().addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    estaNaEstante = true
                } else {
                    estaNaEstante = false
                }
            }
        }
    }

    fun atualizarStatusLivro(novoStatus: String) {
        livro?.let {
            it.volumeInfo?.nome = novoStatus
            db.collection("livros").document(it.document ?: "").set(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Status do livro atualizado para $novoStatus", LENGTH_SHORT).show()
                    livro = it.copy()
                } else {
                    Toast.makeText(context, "Erro ao atualizar o status", LENGTH_SHORT).show()
                }
            }
        }
    }
    LayoutVariant(navController, titulo = if (titulo !== null) titulo else "Não encontrado") {
        if (livro != null) {
            Column {
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 16.dp
                    ),
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(156.dp)
                                .paint(
                                    painterResource(id = R.drawable.bg_livro),
                                    contentScale = ContentScale.FillBounds
                                )
                        ) {
                            if (livro!!.getCapa().isEmpty()) {
                                Image(
                                    painter = painterResource(id = R.drawable.capa_bunny),
                                    contentDescription = stringResource(id = R.string.capa_livro),
                                    modifier = Modifier.height(157.dp).width(104.dp)
                                        .absoluteOffset(y = 28.dp),
                                )
                            } else {
                                AsyncImage(
                                    model = livro!!.getCapa(),
                                    contentDescription = null,
                                    modifier = Modifier.height(157.dp).width(104.dp)
                                        .absoluteOffset(y = 28.dp),
                                )
                            }
                        }
                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                statusOptions.forEach { status ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedStatus = status
                                            atualizarStatusLivro(status)
                                            expanded = false
                                        },
                                        interactionSource = remember { MutableInteractionSource() },
                                        text = { Text(text = status) },
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        leadingIcon = { Icon(imageVector = Icons.Outlined.Star, contentDescription = "Status") },
                                        trailingIcon = { if (selectedStatus == status) Icon(Icons.Default.Check, contentDescription = "Selected") },
                                        enabled = true,
                                        colors = MenuDefaults.itemColors(),  // Alterado para MenuItemColors
                                        contentPadding = PaddingValues(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                        Column(
                            modifier = Modifier.padding(
                                top = 46.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 24.dp
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        text = getNome(),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    Text(
                                        text = livro!!.getAutor(),
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Row {
                                        SuggestionChip(
                                            onClick = {},
                                            label = {
                                                Text(
                                                    text = "Categoria",
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            },
                                            modifier = Modifier.height(16.dp),
                                        )
                                    }
                                }
                                if (estante == true) {
                                    Column {
                                        Box {
                                            TextButton(
                                                onClick = {
                                                    isLoading = true
                                                    favoritarDesfavoritarLivro(
                                                        context,
                                                        livro!!,
                                                        onFavoritado = {
                                                            livro = livro!!.copy(favorito = true)
                                                            icon = Icons.Filled.Favorite
                                                            isLoading = false
                                                        },
                                                        onDesfavoritado = {
                                                            livro = livro!!.copy(favorito = false)
                                                            icon = Icons.Filled.FavoriteBorder
                                                            isLoading = false
                                                        },
                                                        onError = {
                                                            isLoading = false
                                                        }
                                                    )
                                                }
                                            ) {
                                                if (isLoading) {
                                                    CircularProgressIndicator(
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                } else {
                                                    Icon(
                                                        modifier = Modifier.size(28.dp),
                                                        imageVector = icon,
                                                        contentDescription = "favoritar",
                                                    )
                                                    Text(
                                                        text = if (livro!!.favorito == true) "Remover Favorito" else "Adicionar Favorito",
                                                        style = MaterialTheme.typography.labelLarge,
                                                        modifier = Modifier.padding(start = 4.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Box {
                                            Button(
                                                onClick = {
                                                    if (livro!!.disponivelTroca == true) {
                                                    } else {
                                                        trocarLivro(navController, livro!!.id, estante)
                                                    }
                                                }
                                            ) {
                                                Text(text = if (livro!!.disponivelTroca == true) "Cancelar Troca" else "Trocar Livro")
                                            }
                                        }
                                    }
                                } else if (estaNaEstante) {
                                    Button(
                                        onClick = {
                                            if (livro!!.disponivelTroca == true) {
                                            } else {
                                                trocarLivro(navController, livro!!.id, estante)
                                            }
                                        }
                                    ) {
                                        Text(text = if (livro!!.disponivelTroca == true) "Cancelar Troca" else "Trocar Livro")
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            onAdicionarLivro()
                                        }
                                    ) {
                                        Text(text = "Adicionar Livro")
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            IconButton(onClick = { navController.navigate("telaAudioBook/${livro!!.id}") }) {
                                Icon(
                                    modifier = Modifier.size(40.dp),
                                    imageVector = Icons.Filled.Headphones,
                                    contentDescription = "AudioBook"
                                )
                            }

                            Text(
                                text = getSinopse(),
                                maxLines = 4,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .fillMaxWidth(),
                                    imageVector = Icons.Outlined.Star,
                                    contentDescription = "Voto",
                                )
                                Text(
                                    text = "4.5",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                                Text(
                                    text = "(10.000 avaliações)",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "resenhas",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    if (posts.isNotEmpty()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            posts.forEach { post ->
//                                item {
//                                    CardPost(post = post, userName = String())
//                                }
                            }
                        }
                    } else {
                        Text(text = "Ainda não existem postagens.")
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuItem(
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    colors: MenuItemColors = MenuDefaults.itemColors(),  // Corrigido para MenuItemColors
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    // Usando o DropdownMenuItem do material3 corretamente
    androidx.compose.material3.DropdownMenuItem(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding,
        colors = colors,  // Agora passando MenuItemColors
        text = text,  // Passando a função `text`
        leadingIcon = leadingIcon,  // Passando o ícone à esquerda, se presente
        trailingIcon = trailingIcon  // Passando o ícone à direita, se presente
    )
}


//@Preview(showBackground = true)
//@Composable
//private fun GreetingPreview() {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "feedScreen") {
//        composable(
//            route = "feedScreen"
//        ) {
//            FeedScreen(navController)
//        }
//    }
//
//    BookieTheme {
//        TelaLivro(navController, )
//    }
//}