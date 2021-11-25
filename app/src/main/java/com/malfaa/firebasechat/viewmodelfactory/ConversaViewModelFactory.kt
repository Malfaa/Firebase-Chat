package com.malfaa.firebasechat.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfaa.firebasechat.room.MeuDao
import com.malfaa.firebasechat.viewmodel.ConversaViewModel

class ConversaViewModelFactory(private val dataSource: MeuDao): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ConversaViewModel::class.java)){
            return ConversaViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}