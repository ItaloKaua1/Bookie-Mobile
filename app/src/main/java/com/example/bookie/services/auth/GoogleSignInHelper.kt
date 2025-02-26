package com.example.bookie.services.auth

import android.content.Intent
import androidx.activity.ComponentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.example.bookie.R
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInHelper(private val activity: ComponentActivity) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient
    private var onSuccessCallback: ((FirebaseUser) -> Unit)? = null
    private var onErrorCallback: ((Exception) -> Unit)? = null

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.cliente_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun signIn(
        onSuccess: (FirebaseUser) -> Unit,
        onError: (Exception) -> Unit
    ) {
        onSuccessCallback = onSuccess
        onErrorCallback = onError
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun handleSignInResult(data: Intent?) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            onErrorCallback?.invoke(e)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                authResult.user?.let { user ->
                    onSuccessCallback?.invoke(user)
                }
            }
            .addOnFailureListener { exception ->
                onErrorCallback?.invoke(exception)
            }
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }
}