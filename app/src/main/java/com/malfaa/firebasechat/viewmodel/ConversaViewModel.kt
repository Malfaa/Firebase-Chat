package com.malfaa.firebasechat.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.malfaa.firebasechat.fragment.ConversaFragment
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ConversaViewModel(private val meuDao: MeuDao, context: Context) : ViewModel() {

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
  //  @RequiresApi(Build.VERSION_CODES.O)
//    val setHorarioMensagem: LocalDateTime = LocalDateTime.now()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}