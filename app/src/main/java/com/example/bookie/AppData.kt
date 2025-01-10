package com.example.bookie

import com.example.bookie.models.Livro

class AppData {
    private var livros: List<Livro> = listOf()

    companion object {
        @Volatile private var instance: AppData? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AppData().also { instance = it }
        }
    }

    fun setLivros(livros: List<Livro>) {
        this.livros = livros
    }

    fun getLivros(): List<Livro> {
        return livros
    }

    fun getLivro(id: String): Livro? {
        return livros.find { livro -> livro.id == id }
    }
}