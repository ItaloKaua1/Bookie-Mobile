package com.example.bookie.ui.screens.CadastroScreens

import android.content.Context
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
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.bookie.components.SelectPhotoBox
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

private fun updateUserProfile(
    userId: String,
    photoUri: Uri?,
    bio: String,
    context: Context,
    navController: NavHostController
) {
    val db = FirebaseFirestore.getInstance()
    val storageRef = FirebaseStorage.getInstance().reference
    val userRef = db.collection("usuarios").document(userId)

    val updates = hashMapOf<String, Any>()
    updates["bio"] = bio

    if (photoUri != null) {
        val photoRef = storageRef.child("users/$userId/profile_${System.currentTimeMillis()}.jpg")
        photoRef.putFile(photoUri)
            .addOnSuccessListener {
                photoRef.downloadUrl.addOnSuccessListener { uri ->
                    updates["photoUrl"] = uri.toString()
                    userRef.update(updates)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                            navController.navigate("cadastroScreen3") // Alterar para sua tela principal
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erro ao fazer upload da foto: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    } else {
        userRef.update(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                navController.navigate("cadastroScreen3") // Alterar para sua tela principal
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erro ao atualizar perfil: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

@Composable
fun CadastroScreen2(navController: NavHostController) {
    var bio by remember { mutableStateOf("") }
    var profilePictureUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser


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
                    onClick = {
                        if (currentUser == null) {
                            Toast.makeText(context, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        updateUserProfile(
                            userId = currentUser.uid,
                            photoUri = profilePictureUri,
                            bio = bio,
                            context = context,
                            navController = navController
                        )
                    },
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
