package com.malfaa.firebasechat.viewmodel

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

    private val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val num = (1..1000).random()

    fun adicaoDeUserAoFDB(user: FirebaseUser?){
        uiScope.launch {
            val ref = ContatosFragment.database.getReference("Users").child(num.toString())
            val valores = ContatosEntidade(user?.uid.toString()).apply{
                nome = user?.displayName.toString()
                email = user?.email.toString()
                number = num.toString()
            }

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