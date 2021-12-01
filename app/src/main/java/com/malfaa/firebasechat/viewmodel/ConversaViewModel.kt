package com.malfaa.firebasechat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.malfaa.firebasechat.fragment.ConversaFragment
import com.malfaa.firebasechat.getDateTime
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import kotlinx.coroutines.*
import java.util.*


class ConversaViewModel(private val meuDao: MeuDao) : ViewModel() {

    private val args = ConversaFragment.companionArguments.uid
    val recebeConversaRoom = meuDao.receberConversa(args)



    //Coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _num = MutableLiveData<String>()
    val num : LiveData<String>
        get() = _num

    private val _referencia = MutableLiveData<DataSnapshot>()
    val referencia: LiveData<DataSnapshot>
        get() = _referencia

    fun adicionandoMensagem(id: ConversaEntidade){
        uiScope.launch {
            mensagem(id)
        }
    }

    fun retornaNumeroUser(){
        uiScope.launch {
            _num.value = numero(args)
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

    val setHorarioMensagem = getDateTime(Date().time.toString())

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}