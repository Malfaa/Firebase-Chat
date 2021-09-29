package com.malfaa.firebasechat.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malfaa.firebasechat.viewmodel.MenuConversasViewModel
import java.lang.IllegalArgumentException

class MenuConversarsViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MenuConversasViewModel::class.java)){
            return MenuConversasViewModel() as T
        }
        throw IllegalArgumentException("Viewmodel desconhecido")
    }
}