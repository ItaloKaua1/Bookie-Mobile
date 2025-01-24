package com.example.bookie.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.AppData
import com.example.bookie.components.CardConversa
import com.example.bookie.components.CardMensagem
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Conversa
import com.example.bookie.models.Mensagem
import com.example.bookie.models.Notificacao
import com.example.bookie.models.Usuario
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.ref.Reference
import java.util.Date

private fun enviarMensagem(context: Context, corpo: String, id: String?, callback: () -> Unit) {
    var appData = AppData.getInstance()
    val usuario = appData.getUsuarioLogado()

    if (id == null || usuario == null) {
        Toast.makeText(context, "ID e/ou Usuário nulos!", Toast.LENGTH_SHORT).show()
        return;
    }

    var db = FirebaseFirestore.getInstance()

    val mensagem = Mensagem(corpo, usuario, Date())

    db.collection("chatRooms").document(id).update("mensagens", FieldValue.arrayUnion(mensagem)).addOnCompleteListener { it ->
        if (it.isSuccessful) {
//            Toast.makeText(context, "Mensagem enviada com sucesso!", Toast.LENGTH_SHORT).show()
            val notificacao = Notificacao("", usuario.nome, corpo)
            db.collection("notificacoes").add(notificacao)
            callback()
        } else {
            Toast.makeText(context, "Desculpe, ocorreu um erro ao enviar a mensagem", Toast.LENGTH_SHORT).show()

        }
    }
}

private fun enviadoPorMim(mensagem: Mensagem, usuario: Usuario?): Boolean {
    if (usuario!!.id == null || mensagem.usuario == null || mensagem.usuario!!.id == null) {
        return false
    }

    return usuario.id == mensagem.usuario!!.id
}

@Composable
fun TelaConversa(navController: NavController, id: String) {
    var text by remember { mutableStateOf("") }
    var mensagens by remember { mutableStateOf(listOf<Mensagem>()) }
    val appData = AppData.getInstance()
    var conversa = appData.getCOnversaById(id)
    var nome: String = if (conversa!!.usuario2!!.nome != null) conversa.usuario2!!.nome.toString() else ""
    val context = LocalContext.current
    val usuario = appData.getUsuarioLogado()
    var db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        if (conversa.mensagens != null) {
            mensagens = conversa.mensagens!!
        }
    }

    val TAG = "teste-realtime"
    val docRef = db.collection("chatRooms").document(id)
    docRef.addSnapshotListener { snapshot, e ->
        if (e != null) {
            Log.w(TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshot != null && snapshot.exists()) {
            Log.d(TAG, "Current data: ${snapshot.data}")
            val localConversa = snapshot.toObject(Conversa::class.java)
            if (localConversa?.mensagens != null) {
                mensagens = localConversa.mensagens!!
            }


        } else {
            Log.d(TAG, "Current data: null")
        }
    }

    val onEnviarMensagem = { -> text = "" }

    LayoutVariant(navController, nome) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp).weight(1f)
            ) {
                if (mensagens.isEmpty()) {
                    item {
                        Text(text = "Ainda não há mensagens", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    mensagens.forEach { mensagem ->
                        item {
                            Column(
                                horizontalAlignment = if (enviadoPorMim(mensagem, usuario)) Alignment.End else Alignment.Start,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                CardMensagem(mensagem, enviadoPorMim(mensagem, usuario))
                            }
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Escreva uma mensagem...") },
                    modifier = Modifier.weight(1f),
                )
                IconButton(
                    onClick = { enviarMensagem(context, text, conversa.id, onEnviarMensagem) }
                ) {
                    Icon(Icons.Filled.Send, contentDescription = "Enviar")
                }
            }
        }
    }
}