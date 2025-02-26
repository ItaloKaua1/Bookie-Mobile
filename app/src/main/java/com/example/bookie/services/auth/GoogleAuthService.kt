package com.example.bookie.services.auth

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.example.bookie.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

interface GoogleAuthService {
    fun signIn(
        onSuccess: (FirebaseUser) -> Unit,
        onError: (Exception) -> Unit
    )

    suspend fun signOut()

    fun getCurrentUser(): FirebaseUser?

    fun isUserSignedIn(): Boolean
}

class GoogleAuthServiceImpl(
    private val googleSignInHelper: GoogleSignInHelper,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val googleSignInClient: GoogleSignInClient,
    private val context: Context
) : GoogleAuthService {

    override fun signIn(onSuccess: (FirebaseUser) -> Unit, onError: (Exception) -> Unit) {
        try {
            googleSignInHelper.signIn(
                onSuccess = { user ->
                    saveUserInfo(user)
                    onSuccess(user)
                },
                onError = { exception ->
                    onError(exception)
                }
            )
        } catch (e: Exception) {
            onError(e)
        }
    }

    override suspend fun signOut() {
        try {
            auth.signOut()
            googleSignInClient.signOut().await()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun isUserSignedIn(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null && GoogleSignIn.getLastSignedInAccount(context) != null
    }

    private fun saveUserInfo(user: FirebaseUser) {
        val userData = hashMapOf(
            "id" to user.uid,
            "nome" to user.displayName,
            "email" to user.email,
            "photoUrl" to user.photoUrl?.toString(),
            "lastLogin" to System.currentTimeMillis()
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios")
            .document(user.uid)
            .set(userData, SetOptions.merge())
            .addOnFailureListener { e ->
                Log.e("GoogleAuthService", "Erro ao salvar dados do usuário", e)
            }
    }

    companion object {
        private lateinit var instance: GoogleAuthServiceImpl

        fun getInstance(context: Context): GoogleAuthServiceImpl {
            if (!::instance.isInitialized) {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.cliente_id))
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                val googleSignInHelper = GoogleSignInHelper(context as ComponentActivity)

                instance = GoogleAuthServiceImpl(
                    googleSignInHelper = googleSignInHelper,
                    auth = FirebaseAuth.getInstance(),
                    googleSignInClient = googleSignInClient,
                    context = context
                )
            }
            return instance
        }
    }
}