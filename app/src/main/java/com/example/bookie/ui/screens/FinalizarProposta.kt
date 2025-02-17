package com.example.bookie.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookie.AppData
import com.example.bookie.R
import com.example.bookie.UserRepository
import com.example.bookie.components.DropdownInputLivro
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Livro
import com.example.bookie.models.Notificacao
import com.example.bookie.models.TrocaDisponivel
import com.example.bookie.models.TrocaOferecida
import com.example.bookie.models.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

private fun getTroca(id: String): TrocaDisponivel? {
    val appData = AppData.getInstance()

    return appData.getTrocaDisponivel(id)
}

private fun enviarNotificacaoProposta(trocaOferecida: TrocaOferecida, id: String) {
    val db = FirebaseFirestore.getInstance()

    val corpo = "${trocaOferecida.usuario!!.nome} ofereceu trocar o livro ${trocaOferecida.livroOferecido!!.volumeInfo!!.nome} pelo seu livro ${trocaOferecida.trocaDisponivel!!.livro!!.volumeInfo!!.nome}"
    val usuarioNotificacao = trocaOferecida.trocaDisponivel!!.usuario?.id.toString()
    val notificacao = Notificacao("", "Oferta de Troca", corpo, listOf(usuarioNotificacao), trocaOferecida.usuario!!.id, Date(), "VisualizarProposta/$id")
    db.collection("notificacoes").add(notificacao)
}

private fun enviarNotificacaoPropostaAutor(trocaOferecida: TrocaOferecida, id: String) {
    val db = FirebaseFirestore.getInstance()

    val corpo = "Você ofereceu o seu livro ${trocaOferecida.livroOferecido!!.volumeInfo!!.nome} em troca do livro ${trocaOferecida.trocaDisponivel!!.livro!!.volumeInfo!!.nome} de ${trocaOferecida.trocaDisponivel!!.usuario?.id.toString()}"
    val notificacao = Notificacao("", "Oferta de Troca", corpo, listOf(trocaOferecida.usuario!!.id.toString()), trocaOferecida.usuario!!.id, Date(), "VisualizarProposta/$id")
    db.collection("notificacoes").add(notificacao)
}

private fun finalizarTroca(navController: NavController, context: Context, trocaOferecida: TrocaOferecida) {
    val db = FirebaseFirestore.getInstance()

    db.collection("trocas_oferecidas").add(trocaOferecida).addOnCompleteListener { it ->
        if (it.isSuccessful) {
            Toast.makeText(context, "Sucesso ao oferecer troca!", Toast.LENGTH_SHORT).show()
            enviarNotificacaoProposta(trocaOferecida, it.result.id)
            enviarNotificacaoPropostaAutor(trocaOferecida, it.result.id)
            navController.navigate("finalizarPropostaSucesso")
        } else {
            Toast.makeText(context, "Desculpe, ocorreu um erro ao oferecer a troca", Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun FinalizarProposta(navController: NavController, id: String) {
    var observacao by remember { mutableStateOf("") }
    var livros by remember { mutableStateOf(listOf<Livro>()) }
    var db = FirebaseFirestore.getInstance()
    var meuLivroSelecionadoParaTroca by remember { mutableStateOf<Livro?>(null) }
    var troca by remember { mutableStateOf(getTroca(id)) }
    var livro  by remember { mutableStateOf(troca?.livro) }
    val context = LocalContext.current
    var btnEnabled by remember { mutableStateOf(false) }


    val onChangeEstado = { value: Livro -> meuLivroSelecionadoParaTroca = value }

    val onFinalizarTroca = {
        if (troca != null && meuLivroSelecionadoParaTroca != null) {
            GlobalScope.launch {
                val userRepo = UserRepository(context)
                val userName = userRepo.currentUserName.first()
                var userEmail = userRepo.currentUserEmail.first()
                var userId = userRepo.currentUserId.first()
                val usuario = Usuario(userId, userEmail, userName)
                val trocaOferecida = TrocaOferecida(
                    observacao,
                    meuLivroSelecionadoParaTroca,
                    usuario,
                    troca,
                    "aberta",
                    Date()
                )
                finalizarTroca(navController, context, trocaOferecida)
            }
        }
    }

    LaunchedEffect(Unit) {
        db.collection("livros").get().addOnSuccessListener { documents ->
            val localLivros: ArrayList<Livro> = arrayListOf()
            for (document in documents) {
                val localLivro = document.toObject(Livro::class.java)
                localLivro.document = document.id
                localLivros.add(localLivro)
            }
            livros = localLivros.toList()
        }
    }

    LaunchedEffect(meuLivroSelecionadoParaTroca) {
        btnEnabled = meuLivroSelecionadoParaTroca != null
    }

    LayoutVariant(navController, "Finalizar Proposta") {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text(text = "confira se está tudo ok...", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(horizontal = 44.dp).fillMaxWidth(),
            ) {
                DropdownInputLivro(
                    selectedValue = meuLivroSelecionadoParaTroca,
                    options = livros,
                    onValueChangedEvent = onChangeEstado,
                )
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
                    if (livro == null || livro!!.getCapa().isEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.capa_bunny),
                            contentDescription = stringResource(id = R.string.capa_livro),
                            modifier = Modifier.height(120.dp).width(86.dp),
                        )
                    } else {
                        AsyncImage(
                            model = livro!!.getCapa(),
                            contentDescription = null,
                            modifier = Modifier.height(120.dp).width(86.dp)
                        )
                    }
                    livro?.volumeInfo?.nome?.let { Text(text = it, style = MaterialTheme.typography.labelMedium, modifier = Modifier.width(86.dp)) }
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
            OutlinedTextField(
                value = observacao,
                onValueChange = { observacao = it },
                minLines = 5,
                maxLines = 5,
                label = {
                    Text("Observação (opcional)")
                },
                placeholder = { Text("Alguma observação que desejar") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = { onFinalizarTroca() },
                    enabled = btnEnabled,
                ) {
                    Text(text = "Propor Troca")
                }
            }
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun Prev() {
//    BookieTheme {
//        FinalizarProposta()
//    }
//}