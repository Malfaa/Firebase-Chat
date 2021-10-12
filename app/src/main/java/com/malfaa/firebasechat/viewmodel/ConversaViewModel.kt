package com.malfaa.firebasechat.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavGraph
import com.malfaa.firebasechat.fragment.ContatosFragmentDirections
import com.malfaa.firebasechat.fragment.ConversaFragment
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ConversaViewModel(private val meuDao: MeuDao, context: Context) : ViewModel() {

    ///NOVO
    val recebeConversa = meuDao.receberConversa(ConversaFragment().newInstance().toString().toInt()) //??

    //Coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun adicionandoMensagem(mensagem: ConversaEntidade){
        uiScope.launch {
            meuDao.inserirMensagem(mensagem)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val setHorarioMensagem: LocalDateTime = LocalDateTime.now()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}