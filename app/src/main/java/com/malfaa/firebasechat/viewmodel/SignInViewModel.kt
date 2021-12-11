package com.malfaa.firebasechat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.room.entidades.SignInEntidade
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.USERS_REFERENCIA
import com.malfaa.firebasechat.viewmodel.ContatosViewModel.Companion.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignInViewModel(private val meuDao: MeuDao) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private var num = (1..1000).random()


    private val _numero = MutableLiveData<Long>()

    private fun checarDispNum(){
        if (!database.reference.child(_numero.value.toString()).get().isSuccessful){
            assert(true)
        }else{
            num = (1..1000).random()
        }

    }

    fun adicaoDeUserAoFDB(user: FirebaseUser?){
        checarDispNum()
        uiScope.launch {
            val ref = database.getReference(USERS_REFERENCIA).child(num.toString())
            val valores = ContatosEntidade(user?.uid.toString(), user?.displayName.toString(), user?.email.toString(), num.toLong())
            _numero.value = num.toLong()
            ref.setValue(valores)}
    }

    fun adicaoInfosPessoal(user: FirebaseUser?){
        uiScope.launch {
            if (user != null) {
                meuDao.inserirInfos(SignInEntidade(user.uid, num.toLong(), user.displayName.toString(), user.email.toString()))
            }
        }
    }
}