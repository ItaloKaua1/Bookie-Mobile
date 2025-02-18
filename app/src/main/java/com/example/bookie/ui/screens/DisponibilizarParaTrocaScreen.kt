package com.example.bookie.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.AppData
import com.example.bookie.UserRepository
import com.example.bookie.components.DropdownInput
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

private fun enviarNotificacao(trocaDisponivel: TrocaDisponivel, id: String) {
    val db = FirebaseFirestore.getInstance()

    val usuarioOrigem = trocaDisponivel.usuario?.id.toString()
    val corpo = "Você disponibilizou o livro ${trocaDisponivel.livro?.volumeInfo?.nome.toString()} para troca, cique aqui para acompanhar."
    val notificacao = Notificacao("", "Livro Disponível para Troca", corpo, listOf(usuarioOrigem), usuarioOrigem, Date(), "listarLivrosTroca")
    db.collection("notificacoes").add(notificacao)
}

private fun atualizarLivro(livro: Livro?) {
    val db = FirebaseFirestore.getInstance()

    if (livro?.document == null) {
        return;
    }

    livro.disponivelTroca = true
    db.collection("livros").document(livro.document!!).set(livro)
}

private fun disponibilizarParaTroca(navController: NavController, context: Context, trocaDisponivel: TrocaDisponivel) {
    val db = FirebaseFirestore.getInstance()

    db.collection("trocas_disponiveis").add(trocaDisponivel).addOnCompleteListener { it ->
        if (it.isSuccessful) {
            atualizarLivro(trocaDisponivel.livro)
            Toast.makeText(context, "Livro disponibilizado para troca!", Toast.LENGTH_SHORT).show()
            enviarNotificacao(trocaDisponivel, it.result.id)
            navController.popBackStack()
        } else {
            Toast.makeText(context, "Desculpe, ocorreu um erro ao disponibilizar livro para troca", Toast.LENGTH_SHORT).show()
        }
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

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun DisponibilizarParaTrocaScreen(navController: NavController, id: String, estante: Boolean? = false) {
    var observacao by remember { mutableStateOf("") }
    var localizacao by remember { mutableStateOf("") }
    var estadoSelecionado by remember { mutableStateOf("") }
    val listaEstados = listOf("Novo", "Como Novo", "Usado")
    val context = LocalContext.current
    var livro by remember(key1 = "favorito") { mutableStateOf(getLivro(id, estante)) }

    val onChangeEstado = { value: String -> estadoSelecionado = value }

    val onDisponibilizar = {
        GlobalScope.launch {
            val userRepo = UserRepository(context)
            val userName = userRepo.currentUserName.first()
            var userEmail = userRepo.currentUserEmail.first()
            var userId = userRepo.currentUserId.first()
            val usuario = Usuario(id = userId, nome = userName, email = userEmail)
            val livro = getLivro(id, estante)

            if (livro != null) {
                val trocaDisponivel = TrocaDisponivel(estado = estadoSelecionado, observacao = observacao, livro = livro, usuario = usuario, finalizada = false)
                disponibilizarParaTroca(navController, context, trocaDisponivel)
            }
        }
    }

    LayoutVariant(navController, titulo = "Disponibilizar para Troca") {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight().padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DropdownInput(
                    selectedValue = estadoSelecionado,
                    options = listaEstados,
                    label = "Estado",
                    onValueChangedEvent = onChangeEstado,
                )

                OutlinedTextField(
                    value = localizacao,
                    onValueChange = { localizacao = it },
                    minLines = 5,
                    maxLines = 5,
                    label = {
                        Text("Localização")
                    },
                    placeholder = { Text("Ex: Canindé, CE") },
                    modifier = Modifier.fillMaxWidth(),
                )

                OutlinedTextField(
                    value = observacao,
                    onValueChange = { observacao = it },
                    minLines = 5,
                    maxLines = 5,
                    label = {
                        Text("Observação (opcional)")
                    },
                    placeholder = { Text("Ex: Troca por livros de romances") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = { onDisponibilizar() }
                ) {
                    Text(text = "Disponibilizar")
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun Prev() {
//    BookieTheme {
//        DisponibilizarParaTrocaScreen()
//    }
//}