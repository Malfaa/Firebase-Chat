package com.malfaa.firebasechat.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.adapter.ContatosAdapter
import com.malfaa.firebasechat.databinding.MenuConversasFragmentBinding
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.viewmodel.ContatosViewModel
import com.malfaa.firebasechat.viewmodel.ConversaViewModel
import com.malfaa.firebasechat.viewmodelfactory.ContatosViewModelFactory
import com.malfaa.firebasechat.viewmodelfactory.ConversaViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContatosFragment : Fragment() {

    private lateinit var viewModel: ContatosViewModel
    private lateinit var binding: MenuConversasFragmentBinding
    private lateinit var viewModelFactory: ContatosViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.menu_conversas_fragment, container, false)

        //binding.lifecycleOwner = this
        SetupDeVariaveis()

        return binding.root
    }

    fun SetupDeVariaveis(){
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()
        viewModelFactory = ContatosViewModelFactory(dataSource, requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[ContatosViewModel::class.java]


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()
        viewModelFactory = ContatosViewModelFactory(dataSource, requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[ContatosViewModel::class.java]

        binding.viewModel = viewModel*/

        val mAdapter = ContatosAdapter()
        binding.RVContatos.adapter = mAdapter

        viewModel.verificaRecyclerView.observe(viewLifecycleOwner, {
            mAdapter.submitList(it)
        })

        viewModel.navegarAteAdicionar.observe(viewLifecycleOwner,{
            this.findNavController().navigate(ContatosFragmentDirections.actionContatosFragmentToConversaFragment())
            Log.d("SKIPADO", "Pressionado")
            viewModel.voltarDeAdicionar()

        })
    }
}

//lifecycleScope.launch
///lifecycleScope.launch {
    //launch {
//    viewModel.navegarAteAdicionar.collect{
//        findNavController().navigate(ContatosFragmentDirections.actionContatosFragmentToConversaFragment())
//        Log.d("SKIP", "Pulou sem ser pressionado")
//        viewModel.voltarDeAdicionar()
//    }
//} CASO QUEIRA ADICIONAR MAIS LAUNCHS EM UM SCOPE
//            launch {
//
//            }