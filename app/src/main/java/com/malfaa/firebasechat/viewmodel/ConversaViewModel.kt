package com.malfaa.firebasechat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.malfaa.firebasechat.fragment.ContatosFragment
import com.malfaa.firebasechat.fragment.ConversaFragment
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import kotlinx.coroutines.*
import java.util.*

class ConversaViewModel(private val meuDao: MeuDao) : ViewModel() {

    companion object{
        lateinit var ID_MENSAGEM_REFERENCIA: String
        lateinit var conversaId:String
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

    fun conversaUid(iUid: String, fUid: String): String {
        return if (iUid.length > fUid.length){
            iUid + fUid
        }else{
            fUid + iUid
        }
    }

    fun taskConversa() {
        conversaId = conversaUid(ContatosFragment.myUid.toString(), args)

        conversaValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val mensagens = snapshot.children.mapNotNull {
                        it.getValue(ConversaEntidade::class.java)
                    }.toList()
                    ID_MENSAGEM_REFERENCIA = snapshot.children.toString()
                    conversa.postValue(mensagens)
                }
                //adcAoRoom()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "no onCancelled")
            }
        }
        ContatosFragment.database.getReference(ConversaFragment.CONVERSA_REFERENCIA).child(conversaId).addValueEventListener(conversaValueEventListener)
    }

    // FIXME: 06/12/2021 Problema é que as variáveis não estão recebendo seus devidos valores, com isso não está sendo adicionado ao Room function.
    fun adcAoRoom(){
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
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}