package com.malfaa.firebasechat.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.databinding.AdicionaContatoFragmentBinding
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.viewmodel.AdicionaContatoViewModel
import com.malfaa.firebasechat.viewmodel.ContatosViewModel
import com.malfaa.firebasechat.viewmodelfactory.AdicionaContatoViewModelFactory
import com.malfaa.firebasechat.viewmodelfactory.ContatosViewModelFactory

class AdicionaContatoFragment : Fragment() {

    private lateinit var viewModel: AdicionaContatoViewModel
    private lateinit var binding: AdicionaContatoFragmentBinding
    private lateinit var viewModelFactory: AdicionaContatoViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.adiciona_contato_fragment, container, false)

        return binding.root
    }

    private fun SetupVariaveisIniciais(){
        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()
        viewModelFactory = AdicionaContatoViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[AdicionaContatoViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SetupVariaveisIniciais()

        binding.viewModel = viewModel


    }

}