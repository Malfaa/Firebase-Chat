package com.malfaa.firebasechat.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.malfaa.firebasechat.fragment.ContatosFragment
import com.malfaa.firebasechat.fragment.SignInFragmentDirections
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.safeNavigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun adicaoDeUserAoFDB(user: FirebaseUser?){
        uiScope.launch {
            val num = (1..1000).random()
            val ref = ContatosFragment.database.getReference("Users").child(num.toString()) //random number generator para criar o novo user para adicionar
            val valores = ContatosEntidade(user?.uid.toString()).apply{
                nome = user?.displayName.toString()
                email = user?.email.toString()
            }
            ref.setValue(valores)}
    }
}