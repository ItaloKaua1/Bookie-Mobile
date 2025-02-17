package com.example.bookie.models

import android.content.Context
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Verifica se o usuário está logado
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    // Obtém o usuário atual
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    // Obtém o ID do usuário atual
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // Faz logout do usuário
    fun logout(navController: NavController, context: Context) {
        auth.signOut()
    }
}
