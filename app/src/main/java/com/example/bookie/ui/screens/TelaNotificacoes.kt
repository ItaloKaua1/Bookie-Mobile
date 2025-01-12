package com.example.bookie.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.R
import com.example.bookie.components.CardNotificacao
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Livro
import com.example.bookie.models.Notificacao
import com.example.bookie.services.BooksRepositorio
import com.example.bookie.ui.theme.BookieTheme
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date


@Composable
fun TelaNotificacoes(navController: NavController) {
    var db = FirebaseFirestore.getInstance()
    var notificacoes by remember { mutableStateOf(listOf<Notificacao>()) }

    LaunchedEffect(Unit) {
        db.collection("notificacoes").get().addOnSuccessListener { documents ->
            val localNotificacoes: ArrayList<Notificacao> = arrayListOf()
            for (document in documents) {
                Log.e("dados", "${document.id} -> ${document.data}")
                val localNotificacao = document.toObject(Notificacao::class.java)
                localNotificacoes.add(localNotificacao)
            }
            notificacoes = localNotificacoes.toList()
        }
    }

    LayoutVariant(navController, "Notificações") {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            if (notificacoes.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),

                    ) {
                    notificacoes.forEachIndexed() { index, notificacao ->
                        item{
                            CardNotificacao(notificacao)
                            if (index < notificacoes.size - 1) {
                                HorizontalDivider(
                                    thickness = 0.5.dp,
                                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.empty_state),
                    contentDescription = "Não há notificações",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//private fun GreetingPreview() {
//    BookieTheme {
//        TelaNotificacoes()
//    }
//}