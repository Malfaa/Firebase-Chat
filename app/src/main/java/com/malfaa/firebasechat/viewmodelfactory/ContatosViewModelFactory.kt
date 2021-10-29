package com.malfaa.firebasechat.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.viewmodel.ContatosViewModel

class ContatosViewModelFactory(private val meudao: MeuDao): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ContatosViewModel::class.java)){
            return ContatosViewModel(meudao) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}