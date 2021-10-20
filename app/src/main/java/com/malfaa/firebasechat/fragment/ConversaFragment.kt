package com.malfaa.firebasechat.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.adapter.ConversaAdapter
import com.malfaa.firebasechat.databinding.ConversaFragmentBinding
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.room.entidades.ContatosEntidade
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.viewmodel.ConversaViewModel
import com.malfaa.firebasechat.viewmodelfactory.ConversaViewModelFactory
import java.lang.Exception

class ConversaFragment : Fragment() {

    private lateinit var viewModel: ConversaViewModel
    private lateinit var binding: ConversaFragmentBinding
    private lateinit var viewModelFactory: ConversaViewModelFactory

    val args: ConversaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.conversa_fragment, container, false)

        return binding.root
    }

    private fun SetupVariaveisIniciais(){
        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()
        viewModelFactory = ConversaViewModelFactory(dataSource, requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[ConversaViewModel::class.java]
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SetupVariaveisIniciais()

        val mAdapter = ConversaAdapter()
        binding.conversaRecyclerView.adapter = mAdapter
/*
        viewModel.recebeConversa.observe(viewLifecycleOwner, {
            mAdapter.submitList(it.toMutableList())
        })
*/
        try {
            Log.d("recebe", "${args}")
            Log.d("recebe2", "${args.contatoId}")
            Log.d("Conversa", "${viewModel.recebeConversa}")
        }catch (e: Exception){
            Log.d("Error", "Excecao $e")
        }
        binding.enviarBtn.setOnClickListener{
            viewModel.adicionandoMensagem(ConversaEntidade(ContatosEntidade(args.contatoId)).apply {
                mensagem = binding.mensagemEditText.text.toString()
            })
            binding.mensagemEditText.setText("")
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(false){
            override fun handleOnBackPressed() {
                findNavController().navigate(ConversaFragmentDirections.actionConversaFragmentToContatosFragment())
            }
        })
    }
}