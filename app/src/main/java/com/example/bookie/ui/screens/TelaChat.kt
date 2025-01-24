package com.example.bookie.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.AppData
import com.example.bookie.components.AvatarChat
import com.example.bookie.components.CardConversa
import com.example.bookie.components.CardPost
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Conversa
import com.example.bookie.models.Livro
import com.example.bookie.models.Usuario
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore

private fun irParaConversa(navController: NavController, idConversa: String?) {
    if (idConversa == null) {
        return;
    }
    navController.navigate("telaConversa/${idConversa}")
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

@Composable
fun TelaChat(navController: NavController) {
    var text by remember { mutableStateOf("") }
    var amigos by remember { mutableStateOf(listOf<Usuario>()) }
    var conversas by remember { mutableStateOf(listOf<Conversa>()) }
    var db = FirebaseFirestore.getInstance()
    var appData = AppData.getInstance()
    val context = LocalContext.current

    appData.setUsuarioLogado(Usuario("cgyspWh9UPaVllTgaMH92piVl8X2", "teste@email.com", "Teste"))
    val usuarioLogado = appData.getUsuarioLogado()

    LaunchedEffect(Unit) {
        if (usuarioLogado != null) {
            db.collection("usuarios").whereNotEqualTo("id", usuarioLogado.id).get().addOnSuccessListener { documents ->
                val usuarios: ArrayList<Usuario> = arrayListOf()
                for (document in documents) {
                    Log.e("dados", "${document.id} -> ${document.data}")
                    val usuario = document.toObject(Usuario::class.java)
                    usuarios.add(usuario)
                }
                amigos = usuarios.toList()
//            appData.setLivrosEstante(localLivros.toList())
            }

            db.collection("chatRooms").where(
                Filter.or(
                    Filter.equalTo("usuario1.id", usuarioLogado.id),
                    Filter.equalTo("usuario2.id", usuarioLogado.id)
                )
            ).get().addOnSuccessListener { documents ->
                val localConversas: ArrayList<Conversa> = arrayListOf()
                for (document in documents) {
                    Log.e("dados", "${document.id} -> ${document.data}")
                    val conversa = document.toObject(Conversa::class.java)
                    conversa.id = document.id
                    localConversas.add(conversa)
                }
                conversas = localConversas.toList()
                appData.setConversas(conversas.toList())
            }
        }


    }

    val onClickAmigo = { usuario: Usuario -> entrarNaConversa(context, navController, usuarioLogado, usuario) }
    val onClickConversa = { conversa: Conversa -> irParaConversa(navController, conversa.id) }

    LayoutVariant(navController, "Chat") {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Procure pessoas ou chats...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
                modifier = Modifier.fillMaxWidth(),
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp, top = 24.dp),
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                ) {
                    if (amigos.isEmpty()) {
                        item {
                            Text(text = "Ainda não há amigos", style = MaterialTheme.typography.bodyMedium)
                        }
                    } else {
                        amigos.forEach { amigo ->
                            item {
                                AvatarChat(amigo, onClickAmigo)
                            }
                        }
                    }
                }
            }

            Text(text = "Mensagens", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (conversas.isEmpty()) {
                    item {
                        Text(text = "Ainda não há conversas", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    conversas.forEach { conversa ->
                        item {
                            CardConversa(conversa, onClickConversa)
                        }
                    }
                }
            }
        }

    }
}