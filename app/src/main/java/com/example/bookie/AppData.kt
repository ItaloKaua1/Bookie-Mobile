package com.example.bookie


import com.example.bookie.models.Livro


class AppData {
    private var livrosBusca: List<Livro> = listOf()
    private var minhaEstante: List<Livro> = listOf()


    companion object {
        @Volatile private var instance: AppData? = null


        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AppData().also { instance = it }
        }
    }


    fun setLivrosBusca(livros: List<Livro>) {
        this.livrosBusca = livros
    }


    fun getLivrosBusca(): List<Livro> {
        return livrosBusca
    }


    fun getLivroBusca(id: String): Livro? {
        return livrosBusca.find { livro -> livro.id == id }
    }


    fun setLivrosEstante(livros: List<Livro>) {
        this.minhaEstante = livros
    }


    fun getLivroEstante(id: String): Livro? {
        return minhaEstante.find { livro -> livro.id == id }
    }
}
