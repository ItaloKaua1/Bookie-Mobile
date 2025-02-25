package com.example.bookie.ui.screens.ConfigsScreen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookie.UserRepository
import com.example.bookie.components.LayoutVariant
import com.example.bookie.components.SelectPhotoBox
import com.example.bookie.ui.theme.PurpleBookie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun EditarPerfil(navController: NavHostController) {
    var bio by remember { mutableStateOf("") }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val userRepo = UserRepository(context)
    val focusManager = LocalFocusManager.current
    val firebaseAuth = FirebaseAuth.getInstance()

    // Carrega a bio atual e preenche o campo editável
    LaunchedEffect(Unit) {
        userRepo.currentBio.collect { currentBio ->
            bio = currentBio ?: ""
        }
    }

    LayoutVariant(navController, "Editar Perfil", false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { focusManager.clearFocus() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Componente para selecionar/alterar a foto
                SelectPhotoBox(
                    modifier = Modifier.size(130.dp),
                    onPhotoSelected = { uri ->
                        selectedPhotoUri = uri
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo editável para a biografia
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Biografia") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botão para salvar as alterações
                Button(
                    onClick = {
                        val userId = firebaseAuth.currentUser?.uid
                        if (userId != null) {
                            updateUserProfile(
                                userId = userId,
                                photoUri = selectedPhotoUri,
                                bio = bio,
                                context = context,
                                navController = navController
                            )
                        } else {
                            Toast.makeText(context, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleBookie),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Salvar Alterações",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// Função que atualiza a bio e a foto do usuário no Firebase
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

    val updates = hashMapOf<String, Any>(
        "bio" to bio
    )

    if (photoUri != null) {
        // Faz upload da nova foto e obtém a URL para atualizar no banco
        val photoRef = storageRef.child("users/$userId/profile_${System.currentTimeMillis()}.jpg")
        photoRef.putFile(photoUri)
            .addOnSuccessListener {
                photoRef.downloadUrl.addOnSuccessListener { uri ->
                    updates["photoUrl"] = uri.toString()
                    userRef.update(updates)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack() // Retorna à tela anterior ou navega para a tela desejada
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Erro ao atualizar perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erro ao enviar a foto: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    } else {
        // Apenas atualiza a bio, pois nenhuma nova foto foi selecionada
        userRef.update(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erro ao atualizar perfil: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
