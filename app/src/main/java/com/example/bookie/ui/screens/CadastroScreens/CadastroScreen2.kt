package com.example.bookie.ui.screens.CadastroScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bookie.components.BackComponent
import com.example.bookie.ui.theme.PurpleBookie
import android.net.Uri
import com.example.bookie.components.SelectPhotoBox

@Composable
fun CadastroScreen2(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var profilePictureUri by remember { mutableStateOf<Uri?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            BackComponent(navController = navController, title = "Cadastre-se")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 64.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "quase lá...",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = PurpleBookie,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                ) {
                    SelectPhotoBox(
                        modifier = Modifier.size(100.dp),
                        onPhotoSelected = { uri ->
                            profilePictureUri = uri
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "foto de perfil",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                        Text(
                            text = "faça um upload dos seus arquivos ou tire uma foto agora.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Nome de usuário")
                BasicTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                        .padding(8.dp),
                    textStyle = TextStyle.Default.copy(color = Color.Black),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.padding(4.dp)) {
                            if (username.isEmpty()) {
                                Text(text = "Nome", color = Color.Gray)
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = buildAnnotatedString {
                        append("Biografia ")
                        withStyle(style = SpanStyle(color = Color.Gray)) {
                            append("(opcional)")
                        }
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BasicTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                        .padding(8.dp),
                    textStyle = TextStyle.Default.copy(color = Color.Black),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.padding(4.dp)) {
                            if (bio.isEmpty()) {
                                Text(text = "Biografia", color = Color.Gray)
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("cadastroScreen3") },
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
    }
}
