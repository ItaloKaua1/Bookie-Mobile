package com.example.bookie.ui.screens

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.bookie.MainActivity
import com.example.bookie.R
import com.example.bookie.models.AuthManager

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logo = findViewById<ImageView>(R.id.bookie_logo)

        // Configuração de animações
        val fadeIn = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f).apply { duration = 1000 }
        val scaleUpX = ObjectAnimator.ofFloat(logo, "scaleX", 1f, 1.2f).apply { duration = 1000 }
        val scaleUpY = ObjectAnimator.ofFloat(logo, "scaleY", 1f, 1.2f).apply { duration = 1000 }
        val fadeOut = ObjectAnimator.ofFloat(logo, "alpha", 1f, 0f).apply {
            duration = 1000
            startDelay = 1000
        }

        val animatorSet = AnimatorSet().apply {
            playTogether(fadeIn, scaleUpX, scaleUpY)
            play(fadeOut).after(scaleUpX)
        }

        animatorSet.start()

        // Redirecionar após animação
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(
                    "startDestination",
                    if (AuthManager.isUserLoggedIn()) "feedScreen" else "loginScreen"
                )
            }
            startActivity(intent)
            finish()
        }, 3000)
    }
}
