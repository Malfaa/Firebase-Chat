package com.malfaa.firebasechat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.malfaa.firebasechat.converteLongParaString
import com.malfaa.firebasechat.fragment.ContatosFragment
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

    val args = ConversaFragment.companionArguments.uid

    val recebeConversa = meuDao.receberConversa(args)
    //val retornaNumero = meuDao.retornaNumero(args)


    //Coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun adicionandoMensagem(id: ConversaEntidade){
        uiScope.launch {
            meuDao.inserirMensagem(id)
            onCleared()
        }
    }

    fun retornaNumero() {
        uiScope.launch {
            meuDao.retornaNumero(args).number
            onCleared()
        }
    }


    val setHorarioMensagem = converteLongParaString(Date())
    /*
    fun firebase(){
        val referenciaUid = ContatosFragment.database.getReference("Users").child(ContatosFragment.num).child("uid").get()

        val uid: String = referenciaUid.result.value.toString()
        Log.d("UId", uid)
        val conversaId : String

        if(ContatosFragment.selfUid?.length!! < uid.length){
            conversaId = ContatosFragment.selfUid +uid
            Log.d("ConversaId", conversaId)
        }else{
            conversaId = uid+ ContatosFragment.selfUid
            Log.d("ConversaId", conversaId)
        }
        val referenciaMensagem = ContatosFragment.database.getReference("Conversas").child(conversaId)

        val mensagem = ConversaEntidade(uid).apply {
            horario = setHorarioMensagem
            mensagem = binding.mensagemEditText.text.toString()
            myUid
        }

        referenciaMensagem.push().child(
            ContatosFragment.selfUid
            /**talvez usar {num}?**/).setValue(mensagem)

    }*/

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}