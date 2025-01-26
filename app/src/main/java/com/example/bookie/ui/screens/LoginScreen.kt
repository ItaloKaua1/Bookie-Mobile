package com.example.bookie.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.withStyle
import com.example.bookie.R
import com.example.bookie.UserRepository
import com.example.bookie.models.AuthManager
import com.example.bookie.models.Usuario
import com.example.bookie.ui.theme.PurpleBookie
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
private fun getUsuarioData(id: String?, context: Context, navController: NavHostController) {
    if (id == null) {
        Toast.makeText(context, "Erro ao fazer login!", Toast.LENGTH_SHORT).show()
        return
    }

    val db = FirebaseFirestore.getInstance()
    val userRepo = UserRepository(context)

    db.collection("usuarios").document(id).get().addOnCompleteListener { it ->
        if (it.isSuccessful) {
            val usuario = it.result.toObject(Usuario::class.java)

            if (usuario != null) {
                GlobalScope.launch {
                    userRepo.saveUserEmail(usuario.email!!)
                    userRepo.saveUserId(usuario.id!!)
                    userRepo.saveUserName(usuario.nome!!)

                }

                Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                navController.navigate("feedScreen")
            } else {
                Toast.makeText(context, "Erro ao fazer login!", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(context, "Erro ao fazer login!", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun login(email: String, password: String, context: Context, navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { response ->
        if (response.isSuccessful) {

            getUsuarioData(response.result.user!!.uid, context, navController)
        }
    }.addOnFailureListener { response ->
        Toast.makeText(context, "Erro ao fazer login!", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.login2_back),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bookie_logotipo),
                    contentDescription = "Bookie Logo",
                    modifier = Modifier
                        .size(160.dp)
                        .padding(top = 48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Faça seu login!",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = PurpleBookie,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "E-mail")
                        BasicTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                                .padding(8.dp),
                            textStyle = TextStyle.Default.copy(color = Color.Black),
                            decorationBox = { innerTextField ->
                                Box(modifier = Modifier.padding(4.dp)) {
                                    if (email.isEmpty()) {
                                        Text(text = "Email", color = Color.Gray)
                                    }
                                    innerTextField()
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Senha")
                        BasicTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp),
                            textStyle = TextStyle.Default.copy(color = Color.Black),
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            decorationBox = { innerTextField ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        if (password.isEmpty()) {
                                            Text(text = "Senha", color = Color.Gray)
                                        }
                                        innerTextField()
                                    }

                                    IconButton(
                                        onClick = { showPassword = !showPassword }
                                    ) {
                                        Icon(
                                            imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                            contentDescription = "Mostrar/Ocultar Senha"
                                        )
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { login(email, password, context, navController) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                                .height(56.dp)
                                .padding(bottom = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PurpleBookie
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Entrar",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ClickableText(
                                text = buildAnnotatedString {
                                    append("Não tem conta? ")
                                    pushStringAnnotation(tag = "Faça login", annotation = "loginScreen")
                                    withStyle(
                                        style = SpanStyle(
                                            color = PurpleBookie,
                                            textDecoration = TextDecoration.Underline,
                                            fontSize = 16.sp
                                        )
                                    ) {
                                        append("Cadastre-se")
                                    }
                                    pop()
                                },
                                onClick = { offset ->
                                    navController.navigate("cadastroScreen1")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
