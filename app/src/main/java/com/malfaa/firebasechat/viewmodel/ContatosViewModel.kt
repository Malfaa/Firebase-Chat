package com.malfaa.firebasechat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malfaa.firebasechat.fragment.ContatosFragment
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import kotlinx.coroutines.*

class ContatosViewModel(private val meuDao: MeuDao) : ViewModel() {

    //Coroutine ----------------------------------------------------------------------------------
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val myUid = ContatosFragment.myUid.toString()

    private val _meuNum = MutableLiveData<String>()
    val meuNum : LiveData<String>
        get() = _meuNum

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

    fun retornaMeuNumero(){
        uiScope.launch {
            _meuNum.value = retornaNumeroPessoal(myUid)
        }
    }

    private suspend fun retornaNumeroPessoal(uid: String):String{
        return withContext(Dispatchers.IO) {
            val resultado = meuDao.myNum(uid).myNum
            resultado
        }
    }


    val verificaRecyclerView = meuDao.retornarContatos()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}