package com.malfaa.firebasechat.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.room.Repositorio
import com.malfaa.firebasechat.viewmodel.ContatosViewModel
import java.lang.IllegalArgumentException

class ContatosViewModelFactory(private val rep: MeuDao, private val context: Context): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ContatosViewModel::class.java)){
            return ContatosViewModel(rep, context) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}