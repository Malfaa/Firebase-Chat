package com.malfaa.firebasechat.viewmodel

import androidx.lifecycle.ViewModel
import com.malfaa.firebasechat.converteLongParaString
import com.malfaa.firebasechat.fragment.ConversaFragment
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import kotlinx.coroutines.*
import java.util.*


class ConversaViewModel(private val meuDao: MeuDao) : ViewModel() {

    private val args = ConversaFragment.companionArguments.uid
    val recebeConversa = meuDao.receberConversa(args)

    //Coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun adicionandoMensagem(id: ConversaEntidade){
        uiScope.launch {
            mensagem(id)
        }
    }

    fun retornaNumeroUser(){
        uiScope.launch {
            ConversaFragment.num = numero(args)
        }
    }

    private suspend fun numero(uid: String): String {
        return withContext(Dispatchers.IO){
            val num = meuDao.retornaNumero(uid).number
            num
        }
    }

    private suspend fun mensagem(id: ConversaEntidade) {
        return withContext(Dispatchers.IO){
            meuDao.inserirMensagem(id)
            onCleared()
        }
    }

    fun conversaUid(iUid: String, fUid: String): String {
        return if (iUid.length > fUid.length){
            iUid + fUid
        }else{
            fUid + iUid
        }
    }

    val setHorarioMensagem = converteLongParaString(Date())

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}