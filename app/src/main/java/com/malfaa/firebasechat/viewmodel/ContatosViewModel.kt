package com.malfaa.firebasechat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.malfaa.firebasechat.fragment.ContatosFragment
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import kotlinx.coroutines.*

class ContatosViewModel(private val meuDao: MeuDao) : ViewModel() {

    companion object{
        const val CONTATOS_REFERENCIA = "Contatos"
    }

    //Coroutine ----------------------------------------------------------------------------------
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val myUid = ContatosFragment.meuUid.toString()

    private val _meuNum = MutableLiveData<String>()
    val meuNum : LiveData<String>
        get() = _meuNum

    val contatos = MutableLiveData<List<ContatosEntidade>>()
    private lateinit var contatosValueEventListener: ValueEventListener


    fun removeContato(contato: ContatosEntidade){
        uiScope.launch {
            meuDao.removerContato(contato)
            meuDao.removeContato(contato.uid)
            onCleared()
        }
    }

//    //não sei se atualizar contato faz sentido para esse app
//    fun atualizaContato(contato: ContatosEntidade){
//        uiScope.launch {
//            meuDao.atualizarContato(contato)
//            onCleared()
//        }
//    }

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

    fun taskContatos(){
        contatosValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val contato = snapshot.children.mapNotNull {
                        it.getValue(ContatosEntidade::class.java)
                    }.toList()
                    contatos.postValue(contato)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "no onCancelled")
            }
        }
        ContatosFragment.database.getReference(CONTATOS_REFERENCIA).child(meuNum.value.toString()).addValueEventListener(contatosValueEventListener)

    }

    val verificaRecyclerView = meuDao.retornarContatos()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}