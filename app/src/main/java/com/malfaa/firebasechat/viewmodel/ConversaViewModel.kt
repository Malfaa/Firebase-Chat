package com.malfaa.firebasechat.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.malfaa.firebasechat.fragment.ConversaFragment
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

class ConversaViewModel(private val meuDao: MeuDao) : ViewModel() {

    val recebeConversa = meuDao.receberConversa(ConversaFragment.companionArguments.contatoId)

    //Coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun adicionandoMensagem(id: ConversaEntidade){
        uiScope.launch {
            meuDao.inserirMensagem(id)
        }
    }

    // FIXME: 20/10/2021 corrigir horario mensagem
    val setHorarioMensagem = Date().time

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}