package com.malfaa.firebasechat.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.viewmodel.AdicionaContatoViewModel

class AdicionaContatoViewModelFactory (private val meuDao: MeuDao): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AdicionaContatoViewModel::class.java)){
            return AdicionaContatoViewModel(meuDao) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}