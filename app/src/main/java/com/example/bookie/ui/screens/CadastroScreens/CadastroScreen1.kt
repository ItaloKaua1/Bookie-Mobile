package com.example.bookie.ui.screens.CadastroScreens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bookie.components.BackComponent
import com.example.bookie.ui.theme.PurpleBookie
import com.google.firebase.auth.FirebaseAuth
import android.util.Log


private fun registrarUser(email: String, password: String, context: Context, navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { response ->
        if (response.isSuccessful) {
            Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
            navController.navigate("cadastroScreen2")
        }
    }.addOnFailureListener { response ->
        Log.e("tag-register-response-error", response.message!!)
        Toast.makeText(context, "Desculpe, ocorreu um erro ao realizar cadastro.", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun CadastroScreen1(navController: NavHostController) {

    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    Column {
        BackComponent(navController = navController, title = "Cadastre-se")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Pronto pra começar uma nova história?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge.copy(
                color = PurpleBookie,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Nome")
        BasicTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                .padding(8.dp),
            textStyle = TextStyle.Default.copy(color = Color.Black),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.padding(4.dp)) {
                    if (name.isEmpty()) {
                        Text(text = "Nome", color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Email")
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

                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Mostrar/Ocultar Senha"
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Confirme a Senha")
        BasicTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp),
            textStyle = TextStyle.Default.copy(color = Color.Black),
            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (confirmPassword.isEmpty()) {
                            Text(text = "Confirme a senha", color = Color.Gray)
                        }
                        innerTextField()
                    }

                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            imageVector = if (showConfirmPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Mostrar/Ocultar Confirmação de Senha"
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { registrarUser(email, password, context, navController)},
            //onClick = {navController},
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
                text = "Continuar",
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
                    append("Você já possui cadastro? ")
                    pushStringAnnotation(tag = "Faça login", annotation = "loginScreen")
                    withStyle(
                        style = SpanStyle(
                            color = PurpleBookie,
                            textDecoration = TextDecoration.Underline,
                            fontSize = 16.sp
                        )
                    ) {
                        append("Faça login")
                    }
                    pop()
                },
                onClick = { offset ->
                    navController.navigate("loginScreen")
                }
            )
        }
    }
}
