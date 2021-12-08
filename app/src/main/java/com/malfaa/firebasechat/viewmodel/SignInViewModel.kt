package com.malfaa.firebasechat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.malfaa.firebasechat.fragment.ContatosFragment
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.room.entidades.SignInEntidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignInViewModel(private val meuDao: MeuDao) : ViewModel() {

    companion object{
        val ref = ContatosFragment.database
    }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private var num = (1..1000).random() // FIXME: 07/12/2021 quando tiver um número já no db, regenerar random


    private val _numero = MutableLiveData<String>()
    val numero : LiveData<String>
        get() = _numero

    fun checarDispNum(){
        if (!ref.reference.child(_numero.value.toString()).get().isSuccessful){
            assert(true)
        }else{
            num = (1..1000).random() // TODO: 07/12/2021 checar usabilidade
        }

    }

    fun adicaoDeUserAoFDB(user: FirebaseUser?){
        checarDispNum()
        uiScope.launch {
            val ref = ContatosFragment.database.getReference("Users").child(num.toString())
            val valores = ContatosEntidade(user?.uid.toString()).apply{
                nome = user?.displayName.toString()
                email = user?.email.toString()
                number = num.toString()
            }
            _numero.value = num.toString()
            ref.setValue(valores)}
    }

    fun adicaoInfosPessoal(user: FirebaseUser?){
        uiScope.launch {
            if (user != null) {
                meuDao.inserirInfos(SignInEntidade(user.uid, num.toString(), user.displayName.toString(), user.email.toString()))
            }
        }
    }
}