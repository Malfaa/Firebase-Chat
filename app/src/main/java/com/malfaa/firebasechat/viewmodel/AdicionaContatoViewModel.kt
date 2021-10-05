package com.malfaa.firebasechat.viewmodel

import androidx.lifecycle.ViewModel
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AdicionaContatoViewModel(private val meuDao: MeuDao) : ViewModel() {


    //val getAllInfos = meuDao.getAllConversaFromContato()


    //Coroutine ----------------------------------------------------------------------------------
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun recebeContatos() = meuDao.retornarContatos()

    fun adicionaContato(contato: ContatosEntidade){
        uiScope.launch {
            meuDao.novoContato(contato)
        }
    }


}