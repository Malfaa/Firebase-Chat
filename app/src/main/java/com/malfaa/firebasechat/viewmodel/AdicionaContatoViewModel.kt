package com.malfaa.firebasechat.viewmodel

import androidx.lifecycle.ViewModel
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import kotlinx.coroutines.*

class AdicionaContatoViewModel(private val meuDao: MeuDao) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun adicionaContato(contato: ContatosEntidade){
        uiScope.launch {
            adicionaContatoContext(contato)
            onCleared()
        }
    }

    private suspend fun adicionaContatoContext(contato: ContatosEntidade){
        withContext(Dispatchers.IO){
            val novoContato = meuDao.novoContato(contato)
            return@withContext novoContato
        }
    }


}