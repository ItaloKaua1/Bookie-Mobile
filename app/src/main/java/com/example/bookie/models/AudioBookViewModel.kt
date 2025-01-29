package com.example.bookie.models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookie.services.AudioPlayerManager
import com.example.bookie.services.LibriVoxResponse
import com.example.bookie.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.example.bookie.provideXmlExampleApiServices
import com.example.bookie.services.LibriVoxRss
import com.example.bookie.services.RetrofitRss
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

class AudioBookViewModel: ViewModel() {
    //controla a reprodução
    private val audioPlayerManager =  AudioPlayerManager(Application())
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    private val _currentTime = MutableStateFlow(0)
    private val currentTime: StateFlow<Int> = _currentTime.asStateFlow()

    fun pegarMp3() {
        try {
            // Caminho para o arquivo XML
            val filePath = "path/to/your/rss_feed.xml"

            // Cria uma instância de DocumentBuilderFactory
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()

            // Faz o parsing do arquivo XML e obtém o documento
            val document: Document = builder.parse(filePath)

            // Normaliza o documento XML
            document.documentElement.normalize()

            // Obtém todos os elementos <item>
            val itemList: NodeList = document.getElementsByTagName("item")

            // Itera sobre os elementos <item>
            for (i in 0 until itemList.length) {
                val itemNode: Node = itemList.item(i)

                if (itemNode.nodeType == Node.ELEMENT_NODE) {
                    val itemElement = itemNode as Element

                    // Obtém o título do item
                    val title = itemElement.getElementsByTagName("title").item(0).textContent

                    // Verifica se o título é "Letter 2"
                    if (title == "Letter 2") {
                        // Obtém o URL do arquivo .mp3
                        val mp3Url = itemElement.getElementsByTagName("enclosure").item(0).attributes.getNamedItem("url").textContent
                        println("MP3 URL: $mp3Url")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //inicia a reprodução do áudio e atualiza o estado
    fun playAudio(url: String) {
        audioPlayerManager.playAudio(url)
        _isPlaying.value = true
    }

    //pausa
    fun pauseAudio() {
        audioPlayerManager.pauseAudio()
        _isPlaying.value = false
    }

    fun fetchAudioBook(id: String) {
        viewModelScope.launch {
            RetrofitClient.instance.getAudiobook(id, "json").enqueue(object : Callback<LibriVoxResponse> {
                override fun onResponse(call: Call<LibriVoxResponse>, response: Response<LibriVoxResponse>) {
                    if (response.isSuccessful) {
                        val audiobook = response.body()?.books?.firstOrNull()
                        val audioUrl = audiobook?.url_librivox
                        Log.d("AudioBookViewModel", "Audio URL: $audioUrl")
                        if (audioUrl != null) {
                          //  fetchRss()
                        }
                    }
                    Log.d("AudioBookViewModel", "Response: ${response.body()}")
                }
                override fun onFailure(call: Call<LibriVoxResponse>, t: Throwable) {
                    Log.d("AudioBookViewModel", "Error: ${t.message}")
                }
            })
        }
    }

    fun fetchRss(id: String) {
        viewModelScope.launch {
            try {
                    val data = provideXmlExampleApiServices().getAudioBook(52)
                    if (data.isSuccessful) {
                        playAudio(data.body()!!.channel!!.itemList!![0].enclosure!!.url!!)
                        Log.d("getLanguage", "${data.body()!!.channel}")
                    }
            } catch(e:Exception) {
                Log.d("exception", "$e")
            }
        }
    }
}