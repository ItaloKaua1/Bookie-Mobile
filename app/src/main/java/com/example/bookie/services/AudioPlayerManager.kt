package com.example.bookie.services

import android.content.Context
import android.media.MediaPlayer

class AudioPlayerManager(private val context: Context) {
    //usada para reprodução
    private var mediaPlayer: MediaPlayer? = null

    //usada para armazenar posição ao pausar
    private var currentPosition: Int = 0

    //reproduz a partir de uma url
    fun playAudio(url: String) {
        //libera os recursos de reproduções anteriores
        mediaPlayer?.release()
        //cria um novo objeto MediaPlayer e configura
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepare()
            setOnPreparedListener { start() }
        }
    }

    //pausa e salva a posição
    fun pauseAudio() {
        currentPosition = mediaPlayer?.currentPosition ?: 0
        mediaPlayer?.pause()
    }

    //retoma a reprodução de onde parou
    fun resumeAudio() {
        mediaPlayer?.seekTo(currentPosition)
        mediaPlayer?.start()
    }

    //para a reprodução e libera os recursos
    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}