package com.malfaa.firebasechat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.malfaa.firebasechat.fragment.ConversaFragment
import com.malfaa.firebasechat.fragment.ConversaFragment.Companion.binding
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.database
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.myUid
import com.malfaa.firebasechat.viewmodel.LoadingViewModel.Companion.meuNum
import java.util.*

class ConversaViewModel : ViewModel() {

    companion object{
        lateinit var conversaId:String
        const val CONVERSA_REFERENCIA = "Conversas"
        lateinit var setHorarioMensagem:String
    }
    private val args = ConversaFragment.companionArguments.contato

    val conversa = MutableLiveData<List<ConversaEntidade>>()
    private lateinit var conversaValueEventListener: ValueEventListener

    private val _horario = MutableLiveData<Long>()
    val horario: LiveData<Long>
        get() = _horario

    fun retornaHorario(): Long {
        _horario.value = Date().time
        return _horario.value!!
    }


    fun conversaKeyNumber(iNum: Long?, fNum: Long): String{
        val conversaUm = iNum.toString() + fNum.toString()
        val conversaDois = fNum.toString() + iNum.toString()
        return if(conversaUm > conversaDois){
            conversaUm
        }else{
            conversaDois
        }
    }

    fun taskConversa() {
        conversaId = conversaKeyNumber(meuNum.value, args.number)
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

    fun adicionaMensagemAoFirebase(){
        retornaHorario()
        val conversaId = conversaKeyNumber(meuNum.value, args.number)
        val referenciaMensagem = database.getReference(CONVERSA_REFERENCIA).child(conversaId)
        val mensagem = ConversaEntidade(conversaId).apply {
            uid = ConversaFragment.companionArguments.contato.uid
            horario = setHorarioMensagem
            mensagem = binding.mensagemEditText.text.toString()
            meuUid = myUid!!
        }
        referenciaMensagem.push().setValue(mensagem)
    }

}