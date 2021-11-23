package com.malfaa.firebasechat.viewmodel

import androidx.lifecycle.ViewModel
import com.malfaa.firebasechat.converteLongParaString
import com.malfaa.firebasechat.fragment.ConversaFragment
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*


class ConversaViewModel(private val meuDao: MeuDao) : ViewModel() {

    val recebeConversa = meuDao.receberConversa(ConversaFragment.companionArguments.uid)

    //Coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun adicionandoMensagem(id: ConversaEntidade){
        uiScope.launch {
            meuDao.inserirMensagem(id)
        }
    }

    val setHorarioMensagem = converteLongParaString(Date())

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}