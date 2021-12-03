package com.malfaa.firebasechat.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.malfaa.firebasechat.R
import com.malfaa.firebasechat.adapter.ContatosAdapter
import com.malfaa.firebasechat.adapter.ConversaAdapter
import com.malfaa.firebasechat.databinding.ConversaFragmentBinding
import com.malfaa.firebasechat.fragment.ContatosFragment.Companion.database
import com.malfaa.firebasechat.fragment.ContatosFragment.Companion.myUid
import com.malfaa.firebasechat.room.MeuDatabase
import com.malfaa.firebasechat.room.entidades.ConversaEntidade
import com.malfaa.firebasechat.safeNavigate
import com.malfaa.firebasechat.viewmodel.ConversaViewModel
import com.malfaa.firebasechat.viewmodel.ConversaViewModel.Companion.ID_MENSAGEM_REFERENCIA
import com.malfaa.firebasechat.viewmodelfactory.ConversaViewModelFactory

class ConversaFragment : Fragment() {

    private lateinit var viewModel: ConversaViewModel
    private lateinit var binding: ConversaFragmentBinding
    private lateinit var viewModelFactory: ConversaViewModelFactory
    private val args : ConversaFragmentArgs by navArgs()

    companion object{
        lateinit var companionArguments : ConversaFragmentArgs
        const val CONVERSA_REFERENCIA = "Conversas"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.conversa_fragment, container, false)

        companionArguments = args

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val dataSource = MeuDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = ConversaViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)[ConversaViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.retornaNumeroUser()

        val mAdapter = ConversaAdapter()
        binding.conversaRecyclerView.adapter = mAdapter

        viewModel.taskConversa()

        viewModel.conversa.observe(viewLifecycleOwner,{
            mAdapter.submitList(it.asReversed().toMutableList())
        })


//        //Esse funfa pelo ROOM FIXME(não esta funcionando, hue -> o problema está no conversaId, da problema de inicialização por causa do lateinit)
//        viewModel.recebeConversaRoom.observe(viewLifecycleOwner, {
//            mAdapter.submitList(it.toMutableList())
//
//        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            retornaOrdem()
        }
        callback.isEnabled

        binding.enviarBtn.setOnClickListener{
            try{
                adicionaMensagemAoFirebase()
                Log.d("Firebase", "Enviado")
            //aqui

            }catch (e: Exception){
                Log.d("Error", e.toString())
            }
            binding.mensagemEditText.setText("")
        }
    }

    private fun retornaOrdem(){
        this.findNavController().safeNavigate(ConversaFragmentDirections.actionConversaFragmentToContatosFragment())
        ContatosAdapter.usuarioDestino.value = false
    }

    private fun adicionaMensagemAoFirebase(){

        val conversaId = viewModel.conversaUid(myUid.toString(), args.uid)

        val referenciaMensagem = database.getReference(CONVERSA_REFERENCIA).child(conversaId)

        val mensagem = ConversaEntidade(ID_MENSAGEM_REFERENCIA).apply {
            uid = companionArguments.uid
            horario = viewModel.setHorarioMensagem
            mensagem = binding.mensagemEditText.text.toString()
            myUid = ContatosFragment.myUid.toString()
            idConversaGerada = conversaId
        }
        referenciaMensagem.push().setValue(mensagem)
    }

    // TODO: 01/12/2021 colocar foto das pessoas nos contatos (ou não)
}