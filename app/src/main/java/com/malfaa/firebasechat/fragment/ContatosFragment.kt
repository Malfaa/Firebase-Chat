package com.malfaa.firebasechat.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.adapter.ContatosAdapter
import com.malfaa.firebasechat.databinding.ContatosFragmentBinding
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.safeNavigate
import com.malfaa.firebasechat.viewmodel.ContatosViewModel
import com.malfaa.firebasechat.viewmodelfactory.ContatosViewModelFactory

class ContatosFragment : Fragment() {

    private lateinit var viewModel: ContatosViewModel
    private lateinit var binding: ContatosFragmentBinding
    private lateinit var viewModelFactory: ContatosViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.contatos_fragment, container, false)

        return binding.root
    }

    private fun SetupVariaveisIniciais() {
        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()
        viewModelFactory = ContatosViewModelFactory(dataSource, requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[ContatosViewModel::class.java]
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SetupVariaveisIniciais()

        val mAdapter = ContatosAdapter()
        binding.RVContatos.adapter = mAdapter

        viewModel.verificaRecyclerView.observe(viewLifecycleOwner, {
            mAdapter.submitList(it.toMutableList())
        })

        binding.adicaoNovoContato.setOnClickListener {
            findNavController().safeNavigate(ContatosFragmentDirections.actionContatosFragmentToAdicionaContatoFragment())
            viewModel.voltarDeAdicionar()
        }

        ContatosAdapter.usuarioDestino.observe(viewLifecycleOwner, {
            Log.d("Teste","Esta sendo chamado o observe")
            Log.d("Id","${mAdapter.itemCount}")
            val argumento:Int = mAdapter.itemCount
            this.findNavController().navigate(
                ContatosFragmentDirections.actionContatosFragmentToConversaFragment(argumento)) //teste  aqui NOVO //mAdapter.getItemId(id))

            //viewModel.voltarDaConversa()
        })
    }
}
//todo quando clicado aqui, no contato p/ ir até a conversa
//     o recyclerView irá receber os valores adequandos ao id
//      utilizado para conversar

//viewModel.navegarAteConversa.observe(viewLifecycleOwner, {