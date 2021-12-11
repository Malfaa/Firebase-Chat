package com.malfaa.firebasechat.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import kotlinx.coroutines.*

class ContatosViewModel(private val meuDao: MeuDao) : ViewModel() {

    companion object{
        val auth = FirebaseAuth.getInstance()
        val meuUid = FirebaseAuth.getInstance().uid
        val database = Firebase.database
        const val USERS_REFERENCIA = "Users"
        const val UID_REFERENCIA = "uid"
        const val EMAIL_REFERENCIA = "email"
        const val CONTATO_REFERENCIA = "Contatos"

        val referenciaUser = database.reference.child(USERS_REFERENCIA).get().addOnSuccessListener {
            Log.d("Ref", "Dados Recuperados")
        }.addOnFailureListener{
            Log.d("Ref", "Falha em recuperar os dados")
        }
        val referenciaContato = database.getReference(CONTATO_REFERENCIA)

        val usuarioDestino = MutableLiveData<Boolean>()
        lateinit var uidItem : ContatosEntidade
        val deletarUsuario = MutableLiveData<Boolean>()
    }

    val contatos = MutableLiveData<List<ContatosEntidade>>()
    private lateinit var contatosValueEventListener: ValueEventListener

    //Coroutine ----------------------------------------------------------------------------------
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun removeContato(contato: ContatosEntidade){
        uiScope.launch {
            meuDao.removerContato(contato)
            meuDao.removeContato(contato.uid)
            onCleared()
        }
    }

    fun taskContatos(num: String) {
        contatosValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val contato = snapshot.children.mapNotNull {
                        it.getValue(ContatosEntidade::class.java)
                    }.toList()
                    contatos.postValue(contato)
                }
                else{
                    Log.d("Error Task", "Snapshot não existe. Por isso não retorna")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "no onCancelled")
            }
        }
        referenciaContato.child(num).addValueEventListener(contatosValueEventListener) // fixme problema aqui
    }

    fun adicionaAosContatos(){
        uiScope.launch {
            adcAoRoom(contatos.value!!.last())
        }
    }

    private suspend fun adcAoRoom(contato: ContatosEntidade){
        withContext(Dispatchers.IO) {
            val adc = meuDao.novoContato(contato)
            adc
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}