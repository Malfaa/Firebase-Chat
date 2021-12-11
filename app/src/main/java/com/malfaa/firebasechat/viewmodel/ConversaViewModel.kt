package com.malfaa.firebasechat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.malfaa.firebasechat.fragment.ConversaFragment
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.database
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

    private fun retornaHorario(): Long {
        _horario.value = Date().time
        return _horario.value!!
    }


    private fun conversaKeyNumber(iNum: Long?, fNum: Long): String{
        val conversaUm = iNum.toString() + fNum.toString() // 100 + 200 = 100200
        val conversaDois = fNum.toString() + iNum.toString() // 200 + 100 = 200100
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

    fun adicionaMensagemAoFirebase(){ // TODO: 07/12/2021 colocar essa fun no viewmodel

        retornaHorario()

        val conversaId = conversaKeyNumber(meuNum.value, args.number)

        val referenciaMensagem = database.getReference(CONVERSA_REFERENCIA).child(conversaId)

        val mensagem = ConversaEntidade().apply {
            uid = ConversaFragment.companionArguments.contato.uid
            horario = setHorarioMensagem
            mensagem = ConversaFragment().binding.mensagemEditText.text.toString() //aqui ta diferente
            myUid = ContatosViewModel.meuUid.toString()
            idConversaGerada = conversaId
        }
        referenciaMensagem.push().setValue(mensagem)
    }

}