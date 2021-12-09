package com.malfaa.firebasechat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malfaa.firebasechat.room.MeuDao
import kotlinx.coroutines.*

class LoadingViewModel(private val meuDao: MeuDao):ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    companion object{
        private val _meuNum = MutableLiveData<String>()
        val meuNum : LiveData<String>
            get() = _meuNum
    }

    fun retornaMeuNumero(){
        uiScope.launch {
            _meuNum.value = retornaNumeroPessoal()
        }
    }

    private suspend fun retornaNumeroPessoal():String{
        return withContext(Dispatchers.IO) {
            val resultado = meuDao.myNum().myNum
            resultado
        }
    }

}