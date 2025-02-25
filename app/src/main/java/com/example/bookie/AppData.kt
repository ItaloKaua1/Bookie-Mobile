package com.example.bookie


import com.example.bookie.models.Conversa
import com.example.bookie.models.Livro
import com.example.bookie.models.TrocaDisponivel
import com.example.bookie.models.ThematicList
import com.example.bookie.models.Usuario


class AppData {
    private var livrosBusca: List<Livro> = listOf()
    private var minhaEstante: List<Livro> = listOf()
    private var conversas: MutableList<Conversa> = mutableListOf()
    private var usuarioLogado: Usuario? = null
    private var trocasDisponiveis: MutableList<TrocaDisponivel> = mutableListOf()
    private var minhasTrocasDisponiveis: MutableList<TrocaDisponivel> = mutableListOf()
    private var listaTematica: List<ThematicList> = mutableListOf()


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

    fun getConversaByUsers(usuarioId1: String?, usuarioId2: String?): Conversa? {
        if (usuarioId1 == null || usuarioId2 == null) {
            return null
        }

        return conversas.find { conversa -> (conversa.usuario1!!.id == usuarioId1 && conversa.usuario2!!.id == usuarioId2) || (conversa.usuario1!!.id == usuarioId2 && conversa.usuario2!!.id == usuarioId1) }
    }

    fun getCOnversaById(id: String): Conversa? {
        return conversas.find { conversa -> conversa.id == id }
    }

    fun setConversas(conversas: List<Conversa>) {
        this.conversas = conversas.toMutableList()
    }

    fun addConversa(conversa: Conversa) {
        conversas.add(conversa)
    }

    fun getUsuarioLogado(): Usuario? {
        return usuarioLogado
    }

    fun setUsuarioLogado(usuario: Usuario) {
        this.usuarioLogado = usuario
    }

    fun getListaTematica(): List<ThematicList> {
        return listaTematica
    }

    fun setListaTematica(lista: List<ThematicList>) {
        this.listaTematica = lista
    }

    fun getTrocasDisponiveis(): MutableList<TrocaDisponivel> {
        return trocasDisponiveis
    }

    fun setTrocasDisponiveis(trocas: List<TrocaDisponivel>) {
        trocasDisponiveis = trocas.toMutableList()
    }

    fun getTrocaDisponivel(id: String): TrocaDisponivel? {
        return trocasDisponiveis.find { troca -> troca.document == id }
    }

    fun setMinhasTrocasDisponiveis(trocas: List<TrocaDisponivel>) {
        minhasTrocasDisponiveis = trocas.toMutableList()
    }

    fun getMinhasTrocaDisponivel(id: String): TrocaDisponivel? {
        return minhasTrocasDisponiveis.find { troca -> troca.document == id }
    }
}
