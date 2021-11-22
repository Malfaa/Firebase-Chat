package com.malfaa.firebasechat.viewmodel

import androidx.lifecycle.ViewModel
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ContatosViewModel(private val meuDao: MeuDao) : ViewModel() {

    //Coroutine ----------------------------------------------------------------------------------
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun removeContato(contato: ContatosEntidade){
        uiScope.launch {
            meuDao.removerContato(contato)
            meuDao.removeContato(contato.uid)
            onCleared()
        }
    }

    //não sei se atualizar contato faz sentido para esse app
    fun atualizaContato(contato: ContatosEntidade){
        uiScope.launch {
            meuDao.atualizarContato(contato)
            onCleared()
        }
    }


    //Adapter ----------------------------------------------------------------------------------
    val verificaRecyclerView = meuDao.retornarContatos()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}