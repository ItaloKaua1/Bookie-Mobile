package com.example.bookie.ui.screens

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
import androidx.compose.ui.text.withStyle
import com.example.bookie.ui.theme.Purple40
import com.example.bookie.ui.theme.PurpleBookie

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Faça seu login!",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Purple40,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "E-mail")
            // Campo de E-mail
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
            // Campo de Senha com ícone de show/hide dentro do campo
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

            // Botão de Login (Maior e Centralizado)
            Button(
                onClick = { navController.navigate("feedScreen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp),
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

            Spacer(modifier = Modifier.height(16.dp))

            // Texto Clicável de Cadastro
            ClickableText(
                text = buildAnnotatedString {
                    append("Não tem conta? ")
                    pushStringAnnotation(tag = "cadastre-se", annotation = "cadastroScreen1")
                    withStyle(
                        style = SpanStyle(
                            color = Purple40,
                            textDecoration = TextDecoration.Underline,
                            fontSize = 16.sp
                        )
                    ) {
                        append("Cadastre-se")
                    }
                    pop()
                },
                onClick = { offset ->
                    val annotation = buildAnnotatedString {
                        append("Não tem conta? ")
                        pushStringAnnotation(tag = "cadastre-se", annotation = "cadastroScreen1")
                        withStyle(
                            style = SpanStyle(
                                color = Purple40,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Cadastre-se")
                        }
                        pop()
                    }.getStringAnnotations(tag = "cadastre-se", start = offset, end = offset)
                    if (annotation.isNotEmpty()) {
                        navController.navigate("cadastroScreen1")
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
