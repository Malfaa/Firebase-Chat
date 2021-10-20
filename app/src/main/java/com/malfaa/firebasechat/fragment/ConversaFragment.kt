package com.malfaa.firebasechat.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
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

class ConversaFragment : Fragment() {

    private lateinit var viewModel: ConversaViewModel
    private lateinit var binding: ConversaFragmentBinding
    private lateinit var viewModelFactory: ConversaViewModelFactory

    val args : ConversaFragmentArgs by navArgs()

    companion object{
        lateinit var companionArguments : ConversaFragmentArgs
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.conversa_fragment, container, false)

        companionArguments = args

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

        viewModel.recebeConversa.observe(viewLifecycleOwner, {
            mAdapter.submitList(it.toMutableList())
        })

        try {
            Log.d("Conversa:", "${args}")
            Log.d("Conversa ID:", "${args.contatoId}")
            Log.d("Lista Conversa", "${viewModel.recebeConversa.value}")
            Log.d("NewInstance:", "${companionArguments}")
            Log.d("NewInstance Value:", "${companionArguments.contatoId}")
        }catch (e: Exception){
            Log.d("Error", "Excecao $e")
        }

        binding.enviarBtn.setOnClickListener{
            viewModel.adicionandoMensagem(ConversaEntidade(ContatosEntidade(companionArguments.contatoId)).apply {
                // TODO: 20/10/2021 adicionar um outro id que sinaliza quem é o remetente, assim faz o ajuste da tela em Adapter
                mensagem = binding.mensagemEditText.text.toString()
                Log.d("Mensagem:", mensagem)
            })
            binding.mensagemEditText.setText("")
        }
        // TODO: 20/10/2021 Consertar o lado da mensagem referente ao remetente
        // FIXME: 20/10/2021 Consertar backPressedDispatcher
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(false){
            override fun handleOnBackPressed() {
                findNavController().navigate(ConversaFragmentDirections.actionConversaFragmentToContatosFragment())
            }
        })
    }
}