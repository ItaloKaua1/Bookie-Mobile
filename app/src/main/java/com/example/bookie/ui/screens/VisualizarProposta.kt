package com.example.bookie.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookie.AppData
import com.example.bookie.R
import com.example.bookie.UserRepository
import com.example.bookie.components.CardNotificacao
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Conversa
import com.example.bookie.models.Livro
import com.example.bookie.models.Notificacao
import com.example.bookie.models.TrocaDisponivel
import com.example.bookie.models.TrocaOferecida
import com.example.bookie.models.Usuario
import com.example.bookie.ui.theme.BookieTheme
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

private fun enviarNotificacaoRecusaAutor(trocaOferecida: TrocaOferecida, id: String) {
    val db = FirebaseFirestore.getInstance()

    val corpo = "Você recusou a proposta de ${trocaOferecida.usuario!!.nome}, que ofereceu trocar o livro ${trocaOferecida.livroOferecido!!.volumeInfo!!.nome} pelo seu livro ${trocaOferecida.trocaDisponivel!!.livro!!.volumeInfo!!.nome}"
    val usuarioNotificacao = trocaOferecida.trocaDisponivel!!.usuario?.id.toString()
    val notificacao = Notificacao("", "Oferta de Troca Recusada", corpo, listOf(usuarioNotificacao), trocaOferecida.usuario!!.id, Date(), "visualizarProposta/$id")
    db.collection("notificacoes").add(notificacao)
}

private fun enviarNotificacaoRecusa(trocaOferecida: TrocaOferecida, id: String) {
    val db = FirebaseFirestore.getInstance()

    val usuarioOrigem = trocaOferecida.trocaDisponivel!!.usuario?.id.toString()
    val corpo = "${trocaOferecida.trocaDisponivel!!.usuario?.nome.toString()} recusou a proposta de troca do livro ${trocaOferecida.livroOferecido!!.volumeInfo!!.nome} em troca do livro ${trocaOferecida.trocaDisponivel!!.livro!!.volumeInfo!!.nome}"
    val notificacao = Notificacao("", "Oferta de Troca Recusada", corpo, listOf(trocaOferecida.usuario!!.id.toString()), usuarioOrigem, Date(), "visualizarProposta/$id")
    db.collection("notificacoes").add(notificacao)
}

private fun recusarProposta(context: Context, proposta: TrocaOferecida?, id: String): TrocaOferecida? {
    if (proposta == null) {
        return proposta
    }

    val db = FirebaseFirestore.getInstance()

    proposta.status = "recusada"

    db.collection("trocas_oferecidas").document(id).set(proposta).addOnCompleteListener { response ->
        if (response.isSuccessful) {
            Toast.makeText(context, "Proposta recusada!", Toast.LENGTH_SHORT).show()
            enviarNotificacaoRecusaAutor(proposta, id)
            enviarNotificacaoRecusa(proposta, id)
        }
    }

    return proposta
}

private fun enviarNotificacaoAceitaAutor(trocaOferecida: TrocaOferecida, id: String) {
    val db = FirebaseFirestore.getInstance()

    val corpo = "Você aceitou a proposta de ${trocaOferecida.usuario!!.nome}, que ofereceu trocar o livro ${trocaOferecida.livroOferecido!!.volumeInfo!!.nome} pelo seu livro ${trocaOferecida.trocaDisponivel!!.livro!!.volumeInfo!!.nome}"
    val usuarioNotificacao = trocaOferecida.trocaDisponivel!!.usuario?.id.toString()
    val notificacao = Notificacao("", "Oferta de Troca Aceita", corpo, listOf(usuarioNotificacao), trocaOferecida.usuario!!.id, Date(), "visualizarProposta/$id")
    db.collection("notificacoes").add(notificacao)
}

private fun enviarNotificacaoAceita(trocaOferecida: TrocaOferecida, id: String) {
    val db = FirebaseFirestore.getInstance()

    val usuarioOrigem = trocaOferecida.trocaDisponivel!!.usuario?.id.toString()
    val corpo = "${trocaOferecida.trocaDisponivel!!.usuario?.nome.toString()} aceitou a proposta de troca do livro ${trocaOferecida.livroOferecido!!.volumeInfo!!.nome} em troca do livro ${trocaOferecida.trocaDisponivel!!.livro!!.volumeInfo!!.nome}"
    val notificacao = Notificacao("", "Oferta de Troca Aceita", corpo, listOf(trocaOferecida.usuario!!.id.toString()), usuarioOrigem, Date(), "visualizarProposta/$id")
    db.collection("notificacoes").add(notificacao)
}

private fun aceitarProposta(context: Context, proposta: TrocaOferecida?, id: String): TrocaOferecida? {
    if (proposta == null) {
        return proposta
    }

    val db = FirebaseFirestore.getInstance()

    proposta.status = "aceita"

    db.collection("trocas_oferecidas").document(id).set(proposta).addOnCompleteListener { response ->
        if (response.isSuccessful) {
            Toast.makeText(context, "Proposta aceita!", Toast.LENGTH_SHORT).show()
            enviarNotificacaoAceitaAutor(proposta, id)
            enviarNotificacaoAceita(proposta, id)
        }
    }

    return proposta
}

private fun enviarNotificacaoCanceladaAutor(trocaOferecida: TrocaOferecida, id: String) {
    val db = FirebaseFirestore.getInstance()

    val corpo = "Você cancelou a proposta a proposta para trocar o livro ${trocaOferecida.livroOferecido!!.volumeInfo!!.nome} pelo livro ${trocaOferecida.trocaDisponivel!!.livro!!.volumeInfo!!.nome} do usuário ${trocaOferecida.trocaDisponivel!!.usuario!!.nome}"
    val usuarioNotificacao = trocaOferecida.trocaDisponivel!!.usuario?.id.toString()
    val notificacao = Notificacao("", "Oferta de Troca Cancelada", corpo, listOf(trocaOferecida.usuario!!.id.toString()), trocaOferecida.usuario!!.id, Date(), "visualizarProposta/$id")
    db.collection("notificacoes").add(notificacao)
}

private fun enviarNotificacaoCancelada(trocaOferecida: TrocaOferecida, id: String) {
    val db = FirebaseFirestore.getInstance()

    val usuarioOrigem = trocaOferecida.trocaDisponivel!!.usuario?.id.toString()
    val corpo = "${trocaOferecida.usuario?.nome.toString()} cancelou a proposta de troca do livro ${trocaOferecida.livroOferecido!!.volumeInfo!!.nome} em troca do livro ${trocaOferecida.trocaDisponivel!!.livro!!.volumeInfo!!.nome}"
    val notificacao = Notificacao("", "Oferta de Troca Cancelada", corpo, listOf(usuarioOrigem), trocaOferecida.usuario!!.id, Date(), "visualizarProposta/$id")
    db.collection("notificacoes").add(notificacao)
}

private fun cancelarProposta(context: Context, proposta: TrocaOferecida?, id: String): TrocaOferecida? {
    if (proposta == null) {
        return proposta
    }

    val db = FirebaseFirestore.getInstance()

    proposta.status = "cancelada"

    db.collection("trocas_oferecidas").document(id).set(proposta).addOnCompleteListener { response ->
        if (response.isSuccessful) {
            Toast.makeText(context, "Proposta cancelada!", Toast.LENGTH_SHORT).show()
            enviarNotificacaoCanceladaAutor(proposta, id)
            enviarNotificacaoCancelada(proposta, id)
        }
    }

    return proposta
}

private fun enviarNotificacaoTrocaLivro2(trocaOferecida: TrocaOferecida, id: String) {
    val db = FirebaseFirestore.getInstance()

    val usuarioOrigem = trocaOferecida.trocaDisponivel!!.usuario?.id.toString()
    val corpo = "${trocaOferecida.trocaDisponivel!!.usuario?.nome.toString()} marcou a troca como finalizada, por favor verifique se o livro ${trocaOferecida.trocaDisponivel!!.livro!!.volumeInfo!!.nome} está em sua estante."
    val notificacao = Notificacao("", "Troca Finalizada", corpo, listOf(trocaOferecida.usuario!!.id.toString()), usuarioOrigem, Date(), "minhaEstante")
    db.collection("notificacoes").add(notificacao)
}

private fun enviarNotificacaoTrocaLivro1(trocaOferecida: TrocaOferecida, id: String) {
    val db = FirebaseFirestore.getInstance()

    val usuarioOrigem = trocaOferecida.trocaDisponivel!!.usuario?.id.toString()
    val corpo = "Você marcou a troca com ${trocaOferecida.usuario?.nome.toString()} como finalizada, por favor verifique se o livro ${trocaOferecida.livroOferecido!!.volumeInfo!!.nome} está em sua estante."
    val notificacao = Notificacao("", "Troca Finalizada", corpo, listOf(usuarioOrigem), usuarioOrigem, Date(), "minhaEstante")
    db.collection("notificacoes").add(notificacao)
}

private fun trocarLivro2Estante(context: Context, navController: NavController, proposta: TrocaOferecida?, id: String) {
    if (proposta == null) {
        return
    }

    val db = FirebaseFirestore.getInstance()

    val livro = proposta.trocaDisponivel?.livro?.copy()
    if (livro != null) {
        livro.usuario = proposta.usuario

        db.collection("livros").document(livro.document!!).set(livro).addOnCompleteListener { response ->
            if (response.isSuccessful) {
                enviarNotificacaoTrocaLivro1(proposta, id)
                enviarNotificacaoTrocaLivro2(proposta, id)
                Toast.makeText(context, "Troca finalizada!", Toast.LENGTH_SHORT).show()
                navController.navigate("minhaEstante")
            }
        }
    }
}

private fun trocarLivro1Estante(context: Context, navController: NavController, proposta: TrocaOferecida?, id: String) {
    if (proposta == null) {
        return
    }

    val db = FirebaseFirestore.getInstance()

    val livro = proposta.livroOferecido?.copy()
    if (livro != null) {
        livro.usuario = proposta.trocaDisponivel?.usuario
        db.collection("livros").document(livro.document!!).set(livro).addOnCompleteListener { response ->
            if (response.isSuccessful) {
                trocarLivro2Estante(context, navController, proposta, id)
            }
        }
    }
}

private fun trocarLivros(context: Context, navController: NavController, proposta: TrocaOferecida?, id: String) {
    if (proposta == null) {
        return
    }

    val db = FirebaseFirestore.getInstance()

    proposta.status = "finalizada"


    db.collection("trocas_oferecidas").document(id).set(proposta).addOnCompleteListener { response ->
        if (response.isSuccessful) {
            trocarLivro1Estante(context, navController, proposta, id)
        }
    }
}

private fun irParaConversa(navController: NavController, idConversa: String?) {
    if (idConversa == null) {
        return;
    }
    navController.navigate("telaConversa/${idConversa}")
}

private fun buscarConversa(context: Context, navController: NavController, usuario1: Usuario?, usuario2: Usuario) {
    var db = FirebaseFirestore.getInstance()
    var appData = AppData.getInstance()

    if (usuario1 == null) {
        return
    }

    db.collection("chatRooms").where(
        Filter.or(
            Filter.and(
                Filter.equalTo("usuario1.id", usuario1.id),
                Filter.equalTo("usuario2.id", usuario2.id),
            ),
            Filter.and(
                Filter.equalTo("usuario1.id", usuario2.id),
                Filter.equalTo("usuario2.id", usuario1.id),
            )
        )
    ).get().addOnSuccessListener { documents ->
        val localConversas: ArrayList<Conversa> = arrayListOf()
        for (document in documents) {
            val conversa = document.toObject(Conversa::class.java)
            conversa.id = document.id
            localConversas.add(conversa)
        }

        appData.setConversas(localConversas.toList())

        entrarNaConversa(context, navController, usuario1, usuario2)
    }
}

private fun entrarNaConversa(context: Context, navController: NavController, usuario1: Usuario?, usuario2: Usuario) {
    if (usuario1 == null) {
        Toast.makeText(context, "Desculpe, você precisa estar logado para entrar em uma conversa.", Toast.LENGTH_SHORT).show()
        return
    }

    var appData = AppData.getInstance()
    var db = FirebaseFirestore.getInstance()

    var conversa = appData.getConversaByUsers(usuario1.id, usuario2.id)

    if (conversa == null) {
        conversa = Conversa("", listOf(), usuario1, usuario2)

        db.collection("chatRooms").add(conversa).addOnCompleteListener { it ->
            if (it.isSuccessful) {
                Toast.makeText(context, "Conversa criada com sucesso!", Toast.LENGTH_SHORT).show()
                conversa.id = it.result.id
                appData.addConversa(conversa)
                irParaConversa(navController, conversa.id)
            } else {
                Toast.makeText(context, "Desculpe, ocorreu um erro ao criar a conversa", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        irParaConversa(navController, conversa.id)
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun VisualizarProposta(navController: NavController, id: String) {
    var listaStatus by remember { mutableStateOf(listOf<Notificacao>()) }
    val notificacaoInicial = Notificacao(id = "asd", titulo = "Em Espera", usuarioOrigem = "tst", usuarioDestino = listOf("tes"), corpo = "Aguardando Resposta")
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val userRepo = UserRepository(context)

    var proposta  by remember { mutableStateOf<TrocaOferecida?>(null) }
    var minhaProposta by remember { mutableStateOf(false) }
    var outroLivro by remember { mutableStateOf<Livro?>(null) }
    var livroUsuario by remember { mutableStateOf<Livro?>(null) }
    val userIdLocal = userRepo.currentUserId.collectAsState("")

    var onEntrarEmContato = { ->
        val usuario1 = proposta?.trocaDisponivel?.usuario
        val usuario2 = proposta?.usuario

        if (usuario1 != null && usuario2 != null) {
            buscarConversa(context, navController, usuario1, usuario2)
        }
    }

    LaunchedEffect(Unit) {
        launch {
            val userIdLocal = userRepo.currentUserId.first()
            db.collection("trocas_oferecidas").document(id).get().addOnSuccessListener { snapshot ->
                proposta = snapshot.toObject(TrocaOferecida::class.java)
                minhaProposta = proposta?.usuario?.id == userIdLocal

                if (minhaProposta) {
                    livroUsuario = proposta?.livroOferecido
                    outroLivro = proposta?.trocaDisponivel?.livro
                } else {
                    livroUsuario = proposta?.trocaDisponivel?.livro
                    outroLivro = proposta?.livroOferecido
                }
            }
        }
    }

    LaunchedEffect(proposta) {
        if (proposta?.status == "recusada") {
            Log.e("teste", proposta?.status.toString())
            listaStatus = listOf(Notificacao(id = "asd", titulo = "Recusada", usuarioOrigem = "tst", usuarioDestino = listOf("tes"), corpo = "Proposta Recusada", dataHora = proposta?.dataHora))
        } else if (proposta?.status == "cancelada") {
            listaStatus = listOf(Notificacao(id = "asd", titulo = "Cancelada", usuarioOrigem = "tst", usuarioDestino = listOf("tes"), corpo = "Proposta Cancelada", dataHora = proposta?.dataHora))
        } else {
            listaStatus = listOf(notificacaoInicial)
        }
    }

    LayoutVariant(navController, "Visualizar Proposta") {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxHeight()
        ) {
            Text(text = "confira a proposta...", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(horizontal = 44.dp).fillMaxWidth(),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (livroUsuario == null || livroUsuario!!.getCapa().isEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.capa_bunny),
                            contentDescription = stringResource(id = R.string.capa_livro),
                            modifier = Modifier.height(120.dp).width(86.dp),
                        )
                    } else {
                        AsyncImage(
                            model = livroUsuario!!.getCapa(),
                            contentDescription = null,
                            modifier = Modifier.height(120.dp).width(86.dp)
                        )
                    }
                    livroUsuario?.volumeInfo?.nome?.let { Text(text = it, style = MaterialTheme.typography.labelMedium, modifier = Modifier.width(86.dp)) }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Trocando", style = MaterialTheme.typography.labelMedium)
                    Icon(
                        Icons.Filled.SwapHoriz,
                        contentDescription = "Trocar",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (outroLivro == null || outroLivro!!.getCapa().isEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.capa_bunny),
                            contentDescription = stringResource(id = R.string.capa_livro),
                            modifier = Modifier.height(120.dp).width(86.dp),
                        )
                    } else {
                        AsyncImage(
                            model = outroLivro!!.getCapa(),
                            contentDescription = null,
                            modifier = Modifier.height(120.dp).width(86.dp)
                        )
                    }
                    outroLivro?.volumeInfo?.nome?.let { Text(text = it, style = MaterialTheme.typography.labelMedium, modifier = Modifier.width(86.dp)) }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Observação", style = MaterialTheme.typography.labelLarge)
                proposta?.observacao?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
            }

            if (proposta?.status == "finalizada") {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        Icons.Filled.CheckCircleOutline,
                        contentDescription = "Proposta aceita",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(text = "Troca Finalizada", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 16.dp))
                    Text(text = "A troca foi finalizada e o livro trocado já deve estar em sua estante.", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))

                }
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                    Button(
                        onClick = {}
                    ) {
                        Text(text = "Minha Estante")
                    }
                }
            } else if (proposta?.status == "aceita") {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        Icons.Filled.CheckCircleOutline,
                        contentDescription = "Proposta aceita",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(text = "Proposta Aceita", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 16.dp))
                    Text(text = "Entre em contato para marcar a data e local da troca. Só marque a opção recebido após ter certeza que recebeu o livro", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))

                }
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                    Button(
                        onClick = { onEntrarEmContato() }
                    ) {
                        Text(text = "Entrar em Contato")
                    }
                }
            } else if (listaStatus.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Column{
                    Text(text = "Status", style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        listaStatus.forEach { status ->
                            item{
                                CardNotificacao(status)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (proposta?.status == "aceita") {
                    OutlinedButton(
                        onClick = {},
                    ) {
                        Text(text = "Cancelar")
                    }
                    if (!minhaProposta) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { trocarLivros(context, navController, proposta, id) }
                        ) {
                            Text(text = "Troca Realizada")
                        }
                    }
                } else if (proposta?.usuario?.id == userIdLocal.value) {
                    Button(
                        onClick = { proposta = cancelarProposta(context, proposta, id) },
                        enabled = proposta?.status == "aberta"
                    ) {
                        Text(text = "Cancelar Envio")
                    }
                } else {
                    OutlinedButton(
                        onClick = { proposta = recusarProposta(context, proposta, id) },
                        enabled = proposta?.status == "aberta"
                    ) {
                        Text(text = "Recusar")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { proposta = aceitarProposta(context, proposta, id) },
                        enabled = proposta?.status == "aberta"
                    ) {
                        Text(text = "Aceitar")
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun Prev() {
//    BookieTheme {
//        VisualizarProposta(minhaProposta = false, aceita = true)
//    }
//}