package com.malfaa.firebasechat.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.Repositorio
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContatosViewModel(private val rep: MeuDao, context: Context) : ViewModel() {
    //Coroutine ----------------------------------------------------------------------------------
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun adicionaContato(contato: ContatosEntidade){
        uiScope.launch {
            rep.novoContato(contato)
            onCleared()
        }
    }

    fun removeContato(contato: ContatosEntidade){
        uiScope.launch {
            rep.removerContato(contato)
            onCleared()
        }
    }

    fun atualizaContato(contato: ContatosEntidade){
        uiScope.launch {
            rep.atualizarContato(contato)
            onCleared()
        }
    }

    //Navigation ----------------------------------------------------------------------------------
    private val _navegarAteAdicionar = MutableLiveData<Boolean>()
    val navegarAteAdicionar : LiveData<Boolean>
        get()= _navegarAteAdicionar

    fun irAteAdicionar(){
        _navegarAteAdicionar.value = true
    }
    fun voltarDeAdicionar(){
        _navegarAteAdicionar.value = false
    }

    //Adapter ----------------------------------------------------------------------------------
    val verificaRecyclerView = rep.receberContatos()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}