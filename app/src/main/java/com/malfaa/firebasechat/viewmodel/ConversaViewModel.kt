package com.malfaa.firebasechat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.malfaa.firebasechat.fragment.ConversaFragment
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.database
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.meuUid
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.referenciaUser
import kotlinx.coroutines.*
import java.util.*

class ConversaViewModel(private val meuDao: MeuDao) : ViewModel() {

    companion object{
        lateinit var conversaId:String
        const val CONVERSA_REFERENCIA = "Conversas"
        lateinit var setHorarioMensagem:String
    }
    private val args = ConversaFragment.companionArguments.uid

    val conversa = MutableLiveData<List<ConversaEntidade>>()
    private lateinit var conversaValueEventListener: ValueEventListener

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _num = MutableLiveData<String>()

    private val _horario = MutableLiveData<Long>()
    val horario: LiveData<Long>
        get() = _horario

    fun retornaHorario(): Long {
        _horario.value = Date().time
        return _horario.value!!
    }
/*
    fun retornaNumeroUser(){
        uiScope.launch {
            _num.value = numero(args).toString()
        }
    }

    private suspend fun numero(uid: String): Long {
        return withContext(Dispatchers.IO){
            val num = referenciaUser.result.children.mapNotNull {
                it.getVa
            }//meuDao.retornaNumero(uid).number
            num
        }
    }*/

    fun conversaUid(iUid: String, fUid: String): String {
        return if (iUid. > fUid.length){
            iUid + fUid
        }else{
            fUid + iUid
        }
    }

    fun taskConversa() {
        conversaId = conversaUid(meuUid.toString(), args)

        conversaValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val mensagens = snapshot.children.mapNotNull {
                        it.getValue(ConversaEntidade::class.java)
                    }.toList()
                    conversa.postValue(mensagens)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "no onCancelled")
            }
        }
        database.getReference(CONVERSA_REFERENCIA).child(conversaId).addValueEventListener(conversaValueEventListener)
    }

    // FIXME: 06/12/2021 Problema é que as variáveis não estão recebendo seus devidos valores, com isso não está sendo adicionado ao Room function.
    /*fun adcAoRoom(){
        try {
            uiScope.launch {
                conversa.value?.forEach { index ->
                    meuDao.inserirMensagem(ConversaEntidade().apply { //mudei aqui
                        uid = index.uid
                        mensagem = index.mensagem
                        myUid = index.myUid
                        horario = index.horario
                        idConversaGerada = conversaId
                    })
                }
            }
            Log.d("AdcAoRoom", "Adicionado com sucesso")
        }catch (e: Exception){
            Log.d("AdcAoRoom", "Falha ao adicionar")
        }
    }*/

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}