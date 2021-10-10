package com.malfaa.firebasechat.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ConversaViewModel(private val meuDao: MeuDao, context: Context) : ViewModel() {

    val recebeConversa = meuDao.receberConversa()

    //Coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun adicionandoMensagem(mensagem: ConversaEntidade){
        uiScope.launch {
            meuDao.inserirMensagem(mensagem)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}