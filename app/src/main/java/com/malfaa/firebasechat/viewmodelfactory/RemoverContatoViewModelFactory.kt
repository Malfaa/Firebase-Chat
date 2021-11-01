package com.malfaa.firebasechat.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.viewmodel.RemoverContatoViewModel

class RemoverContatoViewModelFactory(private val meuDao: MeuDao):ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RemoverContatoViewModel::class.java)){
            return RemoverContatoViewModel(meuDao) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}