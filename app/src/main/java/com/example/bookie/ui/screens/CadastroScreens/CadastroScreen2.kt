package com.example.bookie.ui.screens.CadastroScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun CadastroScreen2(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var profilePicture by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { profilePicture = !profilePicture },
                contentAlignment = Alignment.Center
            ) {
                if (profilePicture) {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_camera),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Adicionar Foto", color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = username, onValueChange = { username = it }, label = { Text("Nome de Usuário") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Biografia") },
                modifier = Modifier.height(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("CadastroScreen3") }, modifier = Modifier.fillMaxWidth()) {
                Text("Continuar")
            }
            Text(
                    text = "Você já é cadastrado? Faça login",
            color = Color.Blue,
            modifier = Modifier.clickable { navController.navigate("loginScreen") }
            )
        }

    }
}
